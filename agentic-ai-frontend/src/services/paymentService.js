// src/services/paymentService.js

import api from "../api/axios";

// ========================================
// CREATE RAZORPAY PAYMENT ORDER
// ========================================

export const createPaymentOrder = async (orderId) => {

    const response = await api.post(
        "/payments/create-order",
        {
            orderId
        }
    );

    return response.data;
};


// ========================================
// VERIFY RAZORPAY PAYMENT
// ========================================

export const verifyPayment = async (
    razorpayOrderId,
    razorpayPaymentId,
    razorpaySignature
) => {

    const response = await api.post(
        "/payments/verify",
        null,
        {
            params: {
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature
            }
        }
    );

    return response.data;
};