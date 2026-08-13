import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8081/api/v1",
    headers: {
        "Content-Type": "application/json",
    },
});

// ========================================
// REQUEST INTERCEPTOR
// ========================================

api.interceptors.request.use(
    (config) => {

        const accessToken =
            localStorage.getItem("accessToken");

        if (accessToken) {
            config.headers.Authorization =
                `Bearer ${accessToken}`;
        }

        return config;
    },

    (error) => {
        return Promise.reject(error);
    }
);

// ========================================
// RESPONSE INTERCEPTOR
// ========================================

api.interceptors.response.use(

    // Successful response
    (response) => {
        return response;
    },

    // Error response
    async (error) => {

        const originalRequest = error.config;

        // If access token expired
        if (
            error.response?.status === 401 &&
            !originalRequest._retry
        ) {

            originalRequest._retry = true;

            try {

                const refreshToken =
                    localStorage.getItem("refreshToken");

                // No refresh token available
                if (!refreshToken) {

                    localStorage.removeItem("accessToken");
                    localStorage.removeItem("refreshToken");

                    window.location.href = "/login";

                    return Promise.reject(error);
                }

                // Request new access token
                const response = await axios.post(
                    "http://localhost:8081/api/v1/auth/refresh",
                    {
                        refreshToken: refreshToken
                    }
                );

                const newAccessToken =
                    response.data.accessToken;

                const newRefreshToken =
                    response.data.refreshToken;

                // Save new tokens
                localStorage.setItem(
                    "accessToken",
                    newAccessToken
                );

                if (newRefreshToken) {
                    localStorage.setItem(
                        "refreshToken",
                        newRefreshToken
                    );
                }

                // Update original request
                originalRequest.headers.Authorization =
                    `Bearer ${newAccessToken}`;

                // Retry original request
                return api(originalRequest);

            } catch (refreshError) {

                console.error(
                    "Refresh token failed:",
                    refreshError
                );

                localStorage.removeItem("accessToken");
                localStorage.removeItem("refreshToken");

                window.location.href = "/login";

                return Promise.reject(refreshError);
            }
        }

        return Promise.reject(error);
    }
);

export default api;