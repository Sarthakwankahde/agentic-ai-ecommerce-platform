package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.constant.Role;
import com.sarthak.agenticai.dto.ChangePasswordRequestDto;
import com.sarthak.agenticai.dto.ProfileResponseDto;
import com.sarthak.agenticai.dto.UpdateProfileRequestDto;
import com.sarthak.agenticai.dto.UserRequestDto;
import com.sarthak.agenticai.entity.User;
import com.sarthak.agenticai.exception.InvalidCredentialsException;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.exception.UserAlreadyExistsException;
import com.sarthak.agenticai.repository.UserRepository;
import com.sarthak.agenticai.security.JwtService;
import com.sarthak.agenticai.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public String registerUser(UserRequestDto request) {

        Optional<User> existingUser = repository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        repository.save(user);

        return "User Registered Successfully";
    }

    public ProfileResponseDto updateProfile(
            String email,
            UpdateProfileRequestDto request) {
        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        user.setFullName(request.getFullName());
        repository.save(user);

        return new ProfileResponseDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
    public String changePassword(
            String email,
            ChangePasswordRequestDto request) {
        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidCredentialsException(
                    "New password and Confirm password do not match");
        }
        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );
        repository.save(user);
        return "Password changed successfully";
    }

}
