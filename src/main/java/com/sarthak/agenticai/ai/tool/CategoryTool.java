package com.sarthak.agenticai.ai.tool;

import com.sarthak.agenticai.dto.CategoryResponseDto;
import com.sarthak.agenticai.service.CategoryService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryTool {

    private final CategoryService categoryService;

    public CategoryTool(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Tool(description = "Returns all available product categories")
    public String getAllCategories() {

        return buildCategoryResponse(
                categoryService.getAllCategories()
        );
    }

    @Tool(description = "Returns category details by category id")
    public String getCategoryById(Long categoryId) {

        return buildCategoryResponse(
                List.of(categoryService.getCategoryById(categoryId))
        );
    }

    private String buildCategoryResponse(
            List<CategoryResponseDto> categories) {

        if (categories.isEmpty()) {
            return "No categories found.";
        }

        StringBuilder response = new StringBuilder();

        int index = 1;

        for (CategoryResponseDto category : categories) {

            response.append("""
                    
                    Category %d
                    
                    Category ID : %d
                    Category Name : %s
                    
                    """.formatted(
                    index++,
                    category.getId(),
                    category.getName()
            ));
        }

        return response.toString();
    }
}