import api from "../api/axios";

// ========================================
// GET ALL PRODUCTS
// ========================================

export const getAllProducts = async (
    pageNumber = 0,
    pageSize = 5,
    sortBy = "id",
    sortDir = "asc"
) => {

    const response = await api.get("/products", {
        params: {
            pageNumber,
            pageSize,
            sortBy,
            sortDir
        }
    });

    return response.data;
};


// ========================================
// GET PRODUCT BY ID
// ========================================

export const getProductById = async (id) => {

    const response = await api.get(`/products/${id}`);

    return response.data;
};


// ========================================
// SEARCH PRODUCTS
// ========================================

export const searchProducts = async (keyword) => {

    const response = await api.get("/products/search", {
        params: {
            keyword
        }
    });

    return response.data;
};


// ========================================
// GET PRODUCTS BY CATEGORY
// ========================================

export const getProductsByCategory = async (categoryId) => {

    const response = await api.get(
        `/products/category/${categoryId}`
    );

    return response.data;
};


// ========================================
// FILTER PRODUCTS BY PRICE
// ========================================

export const filterProductsByPrice = async (
    minPrice,
    maxPrice
) => {

    const response = await api.get("/products/filter", {
        params: {
            minPrice,
            maxPrice
        }
    });

    return response.data;
};


// ========================================
// GET ALL CATEGORIES
// ========================================

export const getAllCategories = async () => {

    const response = await api.get("/categories");

    return response.data;
};