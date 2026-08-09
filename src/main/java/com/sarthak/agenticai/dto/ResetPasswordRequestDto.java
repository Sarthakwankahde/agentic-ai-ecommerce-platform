package com.sarthak.agenticai.dto;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequestDto {

    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;

    public ResetPasswordRequestDto() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}