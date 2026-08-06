package com.sarthak.agenticai.ai.tool;

import com.sarthak.agenticai.dto.ProductResponseDto;
import com.sarthak.agenticai.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductTool {

    private final ProductService productService;

    public ProductTool(ProductService productService) {
        this.productService = productService;
    }
    private String buildProductResponse(List<ProductResponseDto> products) {

        if (products.isEmpty()) {
            return "No products found.";
        }

        StringBuilder response = new StringBuilder();

        int index = 1;

        for (ProductResponseDto product : products) {

            response.append("""
Product %d
Name : %s
Category : %s
Price : ₹%s
Stock : %s units
Description : %s

"""
                    .formatted(
                            index++,
                            product.getName(),
                            product.getCategoryName(),
                            product.getPrice(),
                            product.getQuantity(),
                            product.getDescription()
                    ));
        }

        return response.toString();
    }
    public String getAllProducts() {

        var products = productService.getAllProducts(
                0,
                10,
                "id",
                "asc"
        );

        return buildProductResponse(products.getContent());
    }
    public String searchProducts(String keyword) {

        return buildProductResponse(
                productService.searchProducts(keyword)
        );
    }
    public String getRecommendedProducts() {

        return buildProductResponse(
                productService.getRecommendedProducts()
        );
    }
    public String getTopCheapProducts() {

        return buildProductResponse(
                productService.getTopCheapProducts()
        );
    }
    public String getTopExpensiveProducts() {

        return buildProductResponse(
                productService.getTopExpensiveProducts()
        );
    }
    public String getAvailableProducts() {

        return buildProductResponse(
                productService.getAvailableProducts()
        );
    }
    public String getProductsByCategory(String categoryName) {

        return buildProductResponse(
                productService.getProductsByCategoryName(categoryName)
        );
    }


}