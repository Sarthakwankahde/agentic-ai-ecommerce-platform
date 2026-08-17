
// src/services/passwordService.js

import api from "../api/axios";


// ========================================
// FORGOT PASSWORD
// ========================================

export const forgotPassword = async (email) => {

    const response = await api.post(
        "/auth/forgot-password",
        {
            email
        }
    );

    return response.data;
};


// ========================================
// RESET PASSWORD
// ========================================

export const resetPassword = async (
    token,
    newPassword,
    confirmPassword
) => {

    const response = await api.post(
        "/auth/reset-password",
        {
            token,
            newPassword,
            confirmPassword
        }
    );

    return response.data;
};