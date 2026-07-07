package com.sarthak.agenticai.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequestDto {

    @NotBlank(message = "Category name is required")
    private String name;

    public CategoryRequestDto() {
    }

    public CategoryRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}