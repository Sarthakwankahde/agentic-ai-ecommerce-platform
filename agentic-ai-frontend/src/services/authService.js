import api from "../api/api";
// ============================================================
// LOGIN
// ============================================================

export const login = async (
    email,
    password
) => {

    const response = await api.post(
        "/auth/login",
        {
            email,
            password
        }
    );

    return response.data;
};


// ============================================================
// REGISTER
// ============================================================

export const register = async (
    fullName,
    email,
    password
) => {

    const response = await api.post(
        "/users/register",
        {
            fullName,
            email,
            password
        }
    );

    return response.data;
};


// ============================================================
// LOGOUT
// ============================================================

export const logout = async () => {

    const response = await api.post(
        "/auth/logout"
    );

    return response.data;
};


// ============================================================
// FORGOT PASSWORD
// ============================================================

export const forgotPassword = async (
    email
) => {

    const response = await api.post(
        "/auth/forgot-password",
        {
            email
        }
    );

    return response.data;
};


// ============================================================
// RESET PASSWORD
// ============================================================

export const resetPassword = async (
    token,
    newPassword
) => {

    const response = await api.post(
        "/auth/reset-password",
        {
            token,
            newPassword
        }
    );

    return response.data;
};


// ============================================================
// REFRESH TOKEN
// ============================================================

export const refreshAccessToken = async (
    refreshToken
) => {

    const response = await api.post(
        "/auth/refresh",
        {
            refreshToken
        }
    );

    return response.data;
};