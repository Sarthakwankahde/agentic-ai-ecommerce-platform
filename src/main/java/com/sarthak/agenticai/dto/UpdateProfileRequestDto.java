package com.sarthak.agenticai.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateProfileRequestDto {

    @NotBlank(message = "Full name is required")
    private String fullName;

    public UpdateProfileRequestDto() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}