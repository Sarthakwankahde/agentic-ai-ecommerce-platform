package com.sarthak.agenticai.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequestDto {

    @NotBlank
    @Email
    private String email;

    public ForgotPasswordRequestDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}