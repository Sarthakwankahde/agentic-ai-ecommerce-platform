package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.config.AppProperties;
import com.sarthak.agenticai.dto.*;
import com.sarthak.agenticai.entity.RefreshToken;
import com.sarthak.agenticai.entity.User;
import com.sarthak.agenticai.exception.InvalidCredentialsException;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.exception.TooManyRequestsException;
import com.sarthak.agenticai.repository.PasswordResetTokenRepository;
import com.sarthak.agenticai.repository.UserRepository;
import com.sarthak.agenticai.security.JwtService;
import com.sarthak.agenticai.security.PasswordResetRateLimiter;
import com.sarthak.agenticai.security.PasswordResetTokenUtil;
import com.sarthak.agenticai.service.AuthService;
import com.sarthak.agenticai.service.EmailService;
import com.sarthak.agenticai.service.RefreshTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sarthak.agenticai.entity.PasswordResetToken;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordResetRateLimiter passwordResetRateLimiter;
    @Value("${app.frontend.url}")
    private String frontendUrl;
    private final AppProperties appProperties;

    public AuthServiceImpl(UserRepository repository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           RefreshTokenService refreshTokenService,PasswordResetTokenRepository passwordResetTokenRepository,
                           EmailService emailService,
                           PasswordResetRateLimiter passwordResetRateLimiter,
                           AppProperties appProperties) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.passwordResetTokenRepository =
                passwordResetTokenRepository;
        this.emailService = emailService;
        this.passwordResetRateLimiter =
                passwordResetRateLimiter;
        this.appProperties = appProperties;
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
        String email =
                request.getEmail()
                        .toLowerCase()
                        .trim();

        if (!passwordResetRateLimiter.isAllowed(email)) {

            throw new TooManyRequestsException(
                    "Too many password reset requests. Please try again later."
            );
        }

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

// Hash the token before storing it in database
        String hashedToken =
                PasswordResetTokenUtil.hashToken(token);

        PasswordResetToken resetToken =
                new PasswordResetToken();

        resetToken.setToken(hashedToken);
        resetToken.setUser(user);
        resetToken.setExpiryDate(
                Instant.now().plus(15, ChronoUnit.MINUTES)
        );

        passwordResetTokenRepository.save(resetToken);

        /*
         * Password reset frontend URL.
         */
        String resetLink =
                appProperties.getFrontendUrl()
                        + "/reset-password?token="
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

        String hashedToken =
                PasswordResetTokenUtil.hashToken(
                        request.getToken()
                );

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByToken(hashedToken)
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
        emailService.sendEmail(
                user.getEmail(),
                "Password Reset Successful - Agentic AI E-Commerce",
                """
                Hi %s,
        
                Your password has been successfully reset.
        
                Your old password can no longer be used.
        
                If you made this change, no further action is required.
        
                If you did not reset your password, please contact support immediately.
        
                Team Agentic AI
                """.formatted(user.getFullName())
        );

        return "Password reset successfully";
    }
    }
