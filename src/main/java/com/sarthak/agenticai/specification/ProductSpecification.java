package com.sarthak.agenticai.specification;

import com.sarthak.agenticai.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasKeyword(String keyword) {

        return (root, query, criteriaBuilder) ->

                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + keyword.toLowerCase() + "%"
                );
    }
    public static Specification<Product> hasCategory(Long categoryId) {

        return (root, query, criteriaBuilder) ->

                criteriaBuilder.equal(
                        root.get("category").get("id"),
                        categoryId
                );
    }
    public static Specification<Product> hasPriceBetween(
            Double minPrice,
            Double maxPrice) {

        return (root, query, criteriaBuilder) ->

                criteriaBuilder.between(
                        root.get("price"),
                        minPrice,
                        maxPrice
                );
    }

}