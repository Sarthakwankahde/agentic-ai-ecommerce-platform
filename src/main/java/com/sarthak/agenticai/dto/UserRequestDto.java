package com.sarthak.agenticai.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequestDto {


    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 50,
            message = "Full name must be between 3 and 50 characters")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20,
            message = "Password must be between 8 and 20 characters")
    private String password;

    // Getter
    public String getFullName() {
        return fullName;
    }

    // Setter
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter
    public String getEmail() {
        return email;
    }

    // Setter
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter
    public String getPassword() {
        return password;
    }

    // Setter
    public void setPassword(String password) {
        this.password = password;
    }
}