package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.ProductRequestDto;
import com.sarthak.agenticai.dto.ProductResponseDto;
import com.sarthak.agenticai.dto.ProductSummaryDto;
import com.sarthak.agenticai.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.sarthak.agenticai.projection.ProductSummary;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create Product (ADMIN)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponseDto createProduct(
            @Valid @RequestBody ProductRequestDto request) {

        return productService.createProduct(request);
    }

    // Get All Products (PUBLIC)
    @GetMapping
    public Page<ProductResponseDto> getAllProducts(

            @RequestParam(defaultValue = "0")
            int pageNumber,

            @RequestParam(defaultValue = "5")
            int pageSize,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String sortDir) {

        return productService.getAllProducts(
                pageNumber,
                pageSize,
                sortBy,
                sortDir
        );
    }
    @GetMapping("/search")
    public List<ProductResponseDto> searchProducts(

            @RequestParam String keyword) {

        return productService.searchProducts(keyword);
    }

    // Get Product By ID (PUBLIC)
    @GetMapping("/{id}")
    public ProductResponseDto getProductById(
            @PathVariable Long id) {

        return productService.getProductById(id);
    }

    // Update Product (ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponseDto updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto request) {

        return productService.updateProduct(id, request);
    }
    @GetMapping("/category/{categoryId}")
    public List<ProductResponseDto> getProductsByCategory(
            @PathVariable Long categoryId) {

        return productService.getProductsByCategory(categoryId);
    }
    @GetMapping("/filter")
    public List<ProductResponseDto> filterProductsByPrice(

            @RequestParam Double minPrice,

            @RequestParam Double maxPrice) {

        return productService.filterProductsByPrice(
                minPrice,
                maxPrice);
    }

    // Delete Product (ADMIN)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);
    }
    @GetMapping("/summary")
    public List<ProductSummary> getProductSummary() {

        return productService.getProductSummary();

    }
    @GetMapping("/summary-dto")
    public List<ProductSummaryDto> getProductSummaryDto() {

        return productService.getProductSummaryDto();

    }
}