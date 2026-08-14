    import api from "../api/axios";

    // ========================================
    // ADD TO CART
    // ========================================

    export const addToCart = async (email, productId, quantity) => {

        const response = await api.post(
            "/cart",
            {
                productId,
                quantity
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
    // GET CART
    // ========================================

    export const getCart = async (email) => {

        const response = await api.get(
            "/cart",
            {
                params: {
                    email
                }
            }
        );

        return response.data;
    };


    // ========================================
    // UPDATE CART ITEM
    // ========================================

    export const updateCartItem = async (
        email,
        cartItemId,
        quantity
    ) => {

        const response = await api.put(
            `/cart/${cartItemId}`,
            null,
            {
                params: {
                    email,
                    quantity
                }
            }
        );

        return response.data;
    };


    // ========================================
    // REMOVE CART ITEM
    // ========================================

    export const removeCartItem = async (
        email,
        cartItemId
    ) => {

        await api.delete(
            `/cart/${cartItemId}`,
            {
                params: {
                    email
                }
            }
        );
    };


    // ========================================
    // CLEAR CART
    // ========================================

    export const clearCart = async (email) => {

        await api.delete(
            "/cart/clear",
            {
                params: {
                    email
                }
            }
        );
    };