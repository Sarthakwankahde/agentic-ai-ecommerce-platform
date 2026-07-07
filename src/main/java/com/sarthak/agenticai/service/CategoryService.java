package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.CategoryRequestDto;
import com.sarthak.agenticai.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto request);

    List<CategoryResponseDto> getAllCategories();

    CategoryResponseDto getCategoryById(Long id);

    CategoryResponseDto updateCategory(Long id, CategoryRequestDto request);

    void deleteCategory(Long id);
    
}