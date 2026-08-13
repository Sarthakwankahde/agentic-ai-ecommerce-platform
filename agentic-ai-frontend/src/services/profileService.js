import api from "../api/axios";

// ========================================
// GET PROFILE
// ========================================

export const getProfile = async () => {

    const response = await api.get("/profile");

    return response.data;
};


// ========================================
// UPDATE PROFILE
// ========================================

export const updateProfile = async (fullName) => {

    const response = await api.put(
        "/profile",
        {
            fullName
        }
    );

    return response.data;
};


// ========================================
// CHANGE PASSWORD
// ========================================

export const changePassword = async (
    currentPassword,
    newPassword,
    confirmPassword
) => {

    const response = await api.put(
        "/profile/change-password",
        {
            currentPassword,
            newPassword,
            confirmPassword
        }
    );

    return response.data;
};