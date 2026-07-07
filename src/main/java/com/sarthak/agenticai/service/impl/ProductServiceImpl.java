package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.ProductRequestDto;
import com.sarthak.agenticai.dto.ProductResponseDto;
import com.sarthak.agenticai.dto.ProductSummaryDto;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.repository.CategoryRepository;
import com.sarthak.agenticai.repository.ProductRepository;
import com.sarthak.agenticai.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.sarthak.agenticai.entity.Category;
import com.sarthak.agenticai.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sarthak.agenticai.specification.ProductSpecification;
import org.springframework.data.jpa.domain.Specification;
import com.sarthak.agenticai.projection.ProductSummary;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);

        ProductResponseDto response = new ProductResponseDto();

        response.setId(savedProduct.getId());
        response.setName(savedProduct.getName());
        response.setDescription(savedProduct.getDescription());
        response.setPrice(savedProduct.getPrice());
        response.setQuantity(savedProduct.getQuantity());
        response.setImageUrl(savedProduct.getImageUrl());
        response.setCategoryName(savedProduct.getCategory().getName());
        return response;
    }

    @Override
    public Page<ProductResponseDto> getAllProducts(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                sort
        );

        Page<Product> productPage =
                productRepository.findAll(pageable);

        return productPage.map(product -> {

            ProductResponseDto response = new ProductResponseDto();

            response.setId(product.getId());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setPrice(product.getPrice());
            response.setQuantity(product.getQuantity());
            response.setImageUrl(product.getImageUrl());
            response.setCategoryName(product.getCategory().getName());

            return response;
        });
    }

    @Override
    public List<ProductResponseDto> searchProducts(String keyword) {

        return productRepository
                .findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(product -> {

                    ProductResponseDto response = new ProductResponseDto();

                    response.setId(product.getId());
                    response.setName(product.getName());
                    response.setDescription(product.getDescription());
                    response.setPrice(product.getPrice());
                    response.setQuantity(product.getQuantity());
                    response.setImageUrl(product.getImageUrl());
                    response.setCategoryName(product.getCategory().getName());

                    return response;

                })
                .toList();
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));
        ProductResponseDto response = new ProductResponseDto();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setImageUrl(product.getImageUrl());
        response.setCategoryName(product.getCategory().getName());

        return response;
    }

    @Override
    public ProductResponseDto updateProduct(Long id,
                                            ProductRequestDto request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        ProductResponseDto response = new ProductResponseDto();

        response.setId(updatedProduct.getId());
        response.setName(updatedProduct.getName());
        response.setDescription(updatedProduct.getDescription());
        response.setPrice(updatedProduct.getPrice());
        response.setQuantity(updatedProduct.getQuantity());
        response.setImageUrl(updatedProduct.getImageUrl());
        response.setCategoryName(updatedProduct.getCategory().getName());

        return response;
    }

    @Override
    public List<ProductResponseDto> getProductsByCategory(Long categoryId) {

        // Check whether category exists
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(product -> {

                    ProductResponseDto response = new ProductResponseDto();

                    response.setId(product.getId());
                    response.setName(product.getName());
                    response.setDescription(product.getDescription());
                    response.setPrice(product.getPrice());
                    response.setQuantity(product.getQuantity());
                    response.setImageUrl(product.getImageUrl());
                    response.setCategoryName(product.getCategory().getName());

                    return response;
                })
                .toList();
    }

    @Override
    public List<ProductResponseDto> filterProductsByPrice(
            Double minPrice,
            Double maxPrice) {

        return productRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(product -> {

                    ProductResponseDto response = new ProductResponseDto();

                    response.setId(product.getId());
                    response.setName(product.getName());
                    response.setDescription(product.getDescription());
                    response.setPrice(product.getPrice());
                    response.setQuantity(product.getQuantity());
                    response.setImageUrl(product.getImageUrl());
                    response.setCategoryName(product.getCategory().getName());

                    return response;
                })
                .toList();
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        productRepository.delete(product);

    }

    @Override
    public Page<ProductResponseDto> filterProducts(

            String keyword,

            Long categoryId,

            Double minPrice,

            Double maxPrice,

            int pageNumber,

            int pageSize,

            String sortBy,

            String sortDir) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> null;

        if (keyword != null && !keyword.isBlank()) {

            specification = specification.and(
                    ProductSpecification.hasKeyword(keyword)
            );
        }

        if (categoryId != null) {

            specification = specification.and(
                    ProductSpecification.hasCategory(categoryId)
            );
        }
        if (minPrice != null && maxPrice != null) {

            specification = specification.and(
                    ProductSpecification.hasPriceBetween(minPrice, maxPrice)
            );
        }
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                sort
        );
        Page<Product> productPage =
                productRepository.findAll(specification, pageable);
        return productPage.map(product -> {

            ProductResponseDto response = new ProductResponseDto();

            response.setId(product.getId());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setPrice(product.getPrice());
            response.setQuantity(product.getQuantity());
            response.setImageUrl(product.getImageUrl());
            response.setCategoryName(product.getCategory().getName());

            return response;
        });
    }
    @Override
    public List<ProductSummary> getProductSummary() {

        return productRepository.findAllBy();

    }
    @Override
    public List<ProductSummaryDto> getProductSummaryDto() {

        return productRepository.getProductSummaryDto();

    }
}