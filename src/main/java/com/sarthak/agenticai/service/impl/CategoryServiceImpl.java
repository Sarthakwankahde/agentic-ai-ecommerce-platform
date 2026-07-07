package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.CategoryRequestDto;
import com.sarthak.agenticai.dto.CategoryResponseDto;
import com.sarthak.agenticai.repository.CategoryRepository;
import com.sarthak.agenticai.service.CategoryService;
import org.springframework.stereotype.Service;
import com.sarthak.agenticai.entity.Category;
import com.sarthak.agenticai.exception.CategoryAlreadyExistsException;
import com.sarthak.agenticai.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto request) {

        if (categoryRepository.existsByName(request.getName())) {
            throw new CategoryAlreadyExistsException(
                    "Category already exists with name : " + request.getName()
            );
        }

        Category category = new Category();

        category.setName(request.getName());

        Category savedCategory = categoryRepository.save(category);

        CategoryResponseDto response = new CategoryResponseDto();

        response.setId(savedCategory.getId());
        response.setName(savedCategory.getName());

        return response;
    }
    @Override
    public CategoryResponseDto getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id));

        CategoryResponseDto response = new CategoryResponseDto();
        response.setId(category.getId());
        response.setName(category.getName());

        return response;
    }
    @Override
    public List<CategoryResponseDto> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(category -> {

                    CategoryResponseDto response = new CategoryResponseDto();

                    response.setId(category.getId());
                    response.setName(category.getName());

                    return response;

                })
                .toList();
    }
    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto request) {

        // 1. Find existing category
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id)
                );

        // 2. Update fields
        category.setName(request.getName());

        // 3. Save updated entity
        Category updatedCategory = categoryRepository.save(category);

        // 4. Convert to DTO
        CategoryResponseDto response = new CategoryResponseDto();
        response.setId(updatedCategory.getId());
        response.setName(updatedCategory.getName());

        return response;
    }
    @Override
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id)
                );

        categoryRepository.delete(category);
    }
}