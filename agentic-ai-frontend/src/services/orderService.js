import api from "../api/axios";

// ========================================
// PLACE ORDER
// ========================================

export const placeOrder = async (email) => {

    const response = await api.post(
        "/orders",
        null,
        {
            params: {
                email
            }
        }
    );

    return response.data;
};


// ========================================
// GET MY ORDERS
// ========================================

export const getMyOrders = async (email) => {

    const response = await api.get(
        "/orders",
        {
            params: {
                email
            }
        }
    );

    return response.data;
};


// ========================================
// GET ORDER BY ID
// ========================================

export const getOrderById = async (
    orderId,
    email
) => {

    const response = await api.get(
        `/orders/${orderId}`,
        {
            params: {
                email
            }
        }
    );

    return response.data;
};


// ========================================
// CANCEL ORDER
// ========================================

export const cancelOrder = async (
    orderId,
    email
) => {

    const response = await api.put(
        `/orders/${orderId}/cancel`,
        null,
        {
            params: {
                email
            }
        }
    );

    return response.data;
};