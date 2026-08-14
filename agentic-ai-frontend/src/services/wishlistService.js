// src/services/wishlistService.js

import api from "../api/axios";

// ========================================
// ADD TO WISHLIST
// ========================================

export const addToWishlist = async (
    email,
    productId
) => {

    const response = await api.post(
        "/wishlist",
        {
            productId
        },
        {
            params: {
                email
            }
        }
    );

    return response.data;
};


// ========================================
// GET WISHLIST
// ========================================

export const getWishlist = async (email) => {

    const response = await api.get(
        "/wishlist",
        {
            params: {
                email
            }
        }
    );

    return response.data;
};


// ========================================
// REMOVE FROM WISHLIST
// ========================================

export const removeFromWishlist = async (
    email,
    wishlistItemId
) => {

    await api.delete(
        `/wishlist/${wishlistItemId}`,
        {
            params: {
                email
            }
        }
    );
};


// ========================================
// CLEAR WISHLIST
// ========================================

export const clearWishlist = async (email) => {

    await api.delete(
        "/wishlist/clear",
        {
            params: {
                email
            }
        }
    );
};