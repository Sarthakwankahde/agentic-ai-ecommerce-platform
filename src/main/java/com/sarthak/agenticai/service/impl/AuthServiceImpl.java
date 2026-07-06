package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.LoginRequestDto;
import com.sarthak.agenticai.dto.LoginResponseDto;
import com.sarthak.agenticai.dto.RefreshTokenRequestDto;
import com.sarthak.agenticai.entity.RefreshToken;
import com.sarthak.agenticai.entity.User;
import com.sarthak.agenticai.exception.InvalidCredentialsException;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.repository.UserRepository;
import com.sarthak.agenticai.security.JwtService;
import com.sarthak.agenticai.service.AuthService;
import com.sarthak.agenticai.service.RefreshTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthServiceImpl(UserRepository repository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,RefreshTokenService refreshTokenService) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

   // @Override
    /*public String login(LoginRequestDto request) {

        Optional<User> optionalUser = repository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            throw new InvalidCredentialsException("Invalid Email or Password");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid Email or Password");
        }

        return jwtService.generateToken(user.getEmail());
    }*/
   @Override
   public LoginResponseDto login(LoginRequestDto request) {

       System.out.println("===== LOGIN DEBUG =====");
       System.out.println("Email from Request : " + request.getEmail());
       System.out.println("Password from Request : " + request.getPassword());

       Optional<User> optionalUser = repository.findByEmail(request.getEmail());

       System.out.println("User Found : " + optionalUser.isPresent());

       if (optionalUser.isEmpty()) {
           throw new InvalidCredentialsException("Invalid Email or Password");
       }

       User user = optionalUser.get();

       System.out.println("Email from DB : " + user.getEmail());
       System.out.println("Password from DB : " + user.getPassword());

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
}