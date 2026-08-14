// src/services/orderService.js

import api from "../api/axios";

// ========================================
// PLACE ORDER
// ========================================

export const placeOrder = async () => {

    const response = await api.post(
        "/orders"
    );

    return response.data;
};


// ========================================
// GET MY ORDERS
// ========================================

export const getMyOrders = async () => {

    const response = await api.get(
        "/orders"
    );

    return response.data;
};


// ========================================
// GET ORDER BY ID
// ========================================

export const getOrderById = async (orderId) => {

    const response = await api.get(
        `/orders/${orderId}`
    );

    return response.data;
};


// ========================================
// CANCEL ORDER
// ========================================

export const cancelOrder = async (orderId) => {

    await api.put(
        `/orders/${orderId}/cancel`
    );
};