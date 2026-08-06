package com.sarthak.agenticai.ai.tool;

import com.sarthak.agenticai.dto.ProductResponseDto;
import com.sarthak.agenticai.service.ProductService;
import org.springframework.ai.tool.annotation.Tool;
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
    @Tool(description = "Returns all available products")
    public String getAllProducts() {

        var products = productService.getAllProducts(
                0,
                10,
                "id",
                "asc"
        );

        return buildProductResponse(products.getContent());
    }

    @Tool(description = "Search products using a keyword")
    public String searchProducts(String keyword) {

        return buildProductResponse(
                productService.searchProducts(keyword)
        );
    }

    @Tool(description = "Returns recommended products")
    public String getRecommendedProducts() {

        return buildProductResponse(
                productService.getRecommendedProducts()
        );
    }

    @Tool(description = "Returns the cheapest available products")
    public String getTopCheapProducts() {

        return buildProductResponse(
                productService.getTopCheapProducts()
        );
    }

    @Tool(description = "Returns the most expensive products")
    public String getTopExpensiveProducts() {

        return buildProductResponse(
                productService.getTopExpensiveProducts()
        );
    }

    @Tool(description = "Returns products currently in stock")
    public String getAvailableProducts() {

        return buildProductResponse(
                productService.getAvailableProducts()
        );
    }

    @Tool(description = "Returns products from a given category")
    public String getProductsByCategory(String categoryName) {

        return buildProductResponse(
                productService.getProductsByCategoryName(categoryName)
        );
    }


}