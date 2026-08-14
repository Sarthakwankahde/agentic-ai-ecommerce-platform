import axios from "axios";

const api = axios.create({

    baseURL: "http://localhost:8081/api/v1",

    headers: {
        "Content-Type": "application/json"
    }
});

api.interceptors.request.use(
    (config) => {

        const token =
            localStorage.getItem(
                "accessToken"
            );

        if (token) {

            config.headers.Authorization =
                `Bearer ${token}`;
        }

        return config;
    },

    (error) => {

        return Promise.reject(error);
    }
);

api.interceptors.response.use(

    (response) => {

        return response;
    },

    (error) => {

        if (
            error.response?.status === 401 &&
            !error.config?.url?.includes(
                "/auth/login"
            )
        ) {

            localStorage.removeItem(
                "accessToken"
            );

            localStorage.removeItem(
                "refreshToken"
            );

            localStorage.removeItem(
                "user"
            );

            localStorage.removeItem(
                "email"
            );
        }

        return Promise.reject(error);
    }
);

export default api;
