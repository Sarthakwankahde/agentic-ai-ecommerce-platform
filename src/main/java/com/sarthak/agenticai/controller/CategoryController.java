package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.CategoryRequestDto;
import com.sarthak.agenticai.dto.CategoryResponseDto;
import com.sarthak.agenticai.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponseDto createCategory(
            @Valid @RequestBody CategoryRequestDto request) {

        return categoryService.createCategory(request);
    }
    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {

        return categoryService.getAllCategories();

    }
    @PutMapping("/{id}")
    public CategoryResponseDto updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto request) {

        return categoryService.updateCategory(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);

    }
    
}