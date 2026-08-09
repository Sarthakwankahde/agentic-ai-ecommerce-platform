package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.*;
import com.sarthak.agenticai.entity.RefreshToken;
import com.sarthak.agenticai.entity.User;
import com.sarthak.agenticai.exception.InvalidCredentialsException;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.repository.PasswordResetTokenRepository;
import com.sarthak.agenticai.repository.UserRepository;
import com.sarthak.agenticai.security.JwtService;
import com.sarthak.agenticai.service.AuthService;
import com.sarthak.agenticai.service.EmailService;
import com.sarthak.agenticai.service.RefreshTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sarthak.agenticai.entity.PasswordResetToken;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository repository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           RefreshTokenService refreshTokenService,PasswordResetTokenRepository passwordResetTokenRepository,
                           EmailService emailService) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.passwordResetTokenRepository =
                passwordResetTokenRepository;
        this.emailService = emailService;
    }
   @Override
   public LoginResponseDto login(LoginRequestDto request) {

       System.out.println("===== LOGIN DEBUG =====");
       System.out.println("Email from Request : " + request.getEmail());

       Optional<User> optionalUser = repository.findByEmail(request.getEmail());

       System.out.println("User Found : " + optionalUser.isPresent());

       if (optionalUser.isEmpty()) {
           throw new InvalidCredentialsException("Invalid Email or Password");
       }

       User user = optionalUser.get();

       System.out.println("Email from DB : " + user.getEmail());

       boolean matched = passwordEncoder.matches(
               request.getPassword(),
               user.getPassword());

       System.out.println("Password Match : " + matched);

       if (!matched) {
           throw new InvalidCredentialsException("Invalid Email or Password");
       }

       System.out.println("Generating JWT...");
       String accessToken = jwtService.generateToken(user.getEmail());
       var refreshToken = refreshTokenService.createRefreshToken(user);
       return new LoginResponseDto(
               accessToken,
               refreshToken.getToken(),
               "Bearer"
       );
   }
    @Override
    public LoginResponseDto refreshToken(RefreshTokenRequestDto request) {
        RefreshToken refreshToken = refreshTokenService
                .verifyRefreshToken(request.getRefreshToken());
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user.getEmail());
        return new LoginResponseDto(
                accessToken,
                refreshToken.getToken(),
                "Bearer"
        );
    }
    @Override
    public void logout(String email) {

        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        refreshTokenService.deleteByUser(user);
    }
    @Override
    public String forgotPassword(
            ForgotPasswordRequestDto request) {

        Optional<User> optionalUser =
                repository.findByEmail(request.getEmail());

        /*
         * Do not reveal whether the email exists.
         */
        if (optionalUser.isEmpty()) {
            return "If the email exists, a password reset link has been sent.";
        }

        User user = optionalUser.get();

        /*
         * Delete an older reset token.
         */
        passwordResetTokenRepository.deleteByUser(user);

        /*
         * Generate secure random token.
         */
        SecureRandom secureRandom = new SecureRandom();

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String token =
                java.util.Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(randomBytes);

        /*
         * Token expires after 15 minutes.
         */
        PasswordResetToken resetToken =
                new PasswordResetToken();

        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(
                Instant.now().plus(15, ChronoUnit.MINUTES)
        );

        passwordResetTokenRepository.save(resetToken);

        /*
         * Password reset frontend URL.
         */
        String resetLink =
                "http://localhost:3000/reset-password?token="
                        + token;

        emailService.sendEmail(
                user.getEmail(),
                "Password Reset - Agentic AI E-Commerce",
                """
                        Hi %s,
                        
                        We received a request to reset your password.
                        
                        Click the link below to reset your password:
                        
                        %s
                        
                        This link will expire in 15 minutes.
                        
                        If you did not request a password reset,
                        you can safely ignore this email.
                        
                        Team Agentic AI
                        """.formatted(
                        user.getFullName(),
                        resetLink
                )
        );

        return "If the email exists, a password reset link has been sent.";
    }
    @Override
    public String resetPassword(
            ResetPasswordRequestDto request) {

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByToken(request.getToken())
                        .orElseThrow(() ->
                                new InvalidCredentialsException(
                                        "Invalid or expired password reset token"
                                )
                        );

        /*
         * Check expiry.
         */
        if (resetToken.getExpiryDate()
                .isBefore(Instant.now())) {

            passwordResetTokenRepository.delete(resetToken);

            throw new InvalidCredentialsException(
                    "Password reset token has expired"
            );
        }

        /*
         * Check password confirmation.
         */
        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new InvalidCredentialsException(
                    "New password and Confirm password do not match"
            );
        }

        User user = resetToken.getUser();

        /*
         * Encode the new password.
         */
        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        repository.save(user);

        /*
         * Token becomes single-use.
         */
        passwordResetTokenRepository.delete(resetToken);

        /*
         * Invalidate existing refresh token.
         *
         * This is important because after a password reset,
         * existing long-lived refresh credentials should not
         * remain active.
         */
        refreshTokenService.deleteByUser(user);

        return "Password reset successfully";
    }
    }
