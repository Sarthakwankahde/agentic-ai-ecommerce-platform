import api from "../api/axios";

// ========================================
// ADD ADDRESS
// ========================================

export const addAddress = async (
    email,
    address
) => {

    const response = await api.post(
        "/addresses",
        address,
        {
            params: {
                email
            }
        }
    );

    return response.data;
};


// ========================================
// GET MY ADDRESSES
// ========================================

export const getMyAddresses = async (email) => {

    const response = await api.get(
        "/addresses",
        {
            params: {
                email
            }
        }
    );

    return response.data;
};


// ========================================
// UPDATE ADDRESS
// ========================================

export const updateAddress = async (
    addressId,
    email,
    address
) => {

    const response = await api.put(
        `/addresses/${addressId}`,
        address,
        {
            params: {
                email
            }
        }
    );

    return response.data;
};


// ========================================
// DELETE ADDRESS
// ========================================

export const deleteAddress = async (
    addressId,
    email
) => {

    const response = await api.delete(
        `/addresses/${addressId}`,
        {
            params: {
                email
            }
        }
    );

    return response.data;
};


// ========================================
// SET DEFAULT ADDRESS
// ========================================

export const setDefaultAddress = async (
    addressId,
    email
) => {

    const response = await api.put(
        `/addresses/${addressId}/default`,
        null,
        {
            params: {
                email
            }
        }
    );

    return response.data;
};