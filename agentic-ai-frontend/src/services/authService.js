import api from "./api";

// ================================
// LOGIN
// ================================

export const login = async (email, password) => {

    const response = await api.post("/auth/login", {
        email,
        password
    });

    return response.data;
};


// ================================
// REGISTER
// ================================

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