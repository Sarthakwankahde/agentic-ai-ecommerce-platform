    package com.sarthak.agenticai.service;

    import com.sarthak.agenticai.dto.ProductRequestDto;
    import com.sarthak.agenticai.dto.ProductResponseDto;
    import org.springframework.data.domain.Page;
    import com.sarthak.agenticai.projection.ProductSummary;
    import com.sarthak.agenticai.dto.ProductSummaryDto;

    import java.util.List;

    public interface ProductService {

        ProductResponseDto createProduct(ProductRequestDto request);
        Page<ProductResponseDto> getAllProducts(
                int pageNumber,
                int pageSize,
                String sortBy,
                String sortDir
        );

        ProductResponseDto getProductById(Long id);
        List<ProductResponseDto> searchProducts(String keyword);
        List<ProductResponseDto> getProductsByCategory(Long categoryId);
        List<ProductResponseDto> filterProductsByPrice(
                Double minPrice,
                Double maxPrice);

        ProductResponseDto updateProduct(Long id,
                                         ProductRequestDto request);

        void deleteProduct(Long id);
        Page<ProductResponseDto> filterProducts(

                String keyword,

                Long categoryId,

                Double minPrice,

                Double maxPrice,

                int pageNumber,

                int pageSize,

                String sortBy,

                String sortDir
        );
        List<ProductSummary> getProductSummary();
        List<ProductSummaryDto> getProductSummaryDto();
        List<ProductResponseDto> getRecommendedProducts();

        List<ProductResponseDto> getTopCheapProducts();

        List<ProductResponseDto> getTopExpensiveProducts();

        List<ProductResponseDto> getAvailableProducts();

        List<ProductResponseDto> getProductsByCategoryName(String categoryName);

    }