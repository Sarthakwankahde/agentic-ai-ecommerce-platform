import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { login as loginApi } from "../services/authService";
import { useAuth } from "../context/AuthContext";

function Login() {

    const navigate = useNavigate();
    const { login } = useAuth();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);


    // ========================================
    // HANDLE LOGIN
    // ========================================

    const handleLogin = async (e) => {

        e.preventDefault();

        setError("");
        setLoading(true);

        try {

            const data = await loginApi(
                email,
                password
            );

            console.log(
                "Login Response:",
                data
            );


            // ========================================
            // SAVE AUTHENTICATION TOKENS
            // ========================================

            login(
                data.accessToken,
                data.refreshToken
            );


            // ========================================
            // SAVE EMAIL
            // Used for cart operations
            // ========================================

            localStorage.setItem(
                "email",
                email
            );


            // ========================================
            // LOGIN SUCCESSFUL
            // ========================================

            navigate("/");


        } catch (error) {

            console.error(
                "Login Error:",
                error
            );


            // ========================================
            // HANDLE SERVER ERROR
            // ========================================

            if (error.response) {

                setError(
                    error.response.data?.message ||
                    "Invalid email or password"
                );

            } else {

                setError(
                    "Unable to connect to server"
                );
            }


        } finally {

            setLoading(false);
        }
    };


    // ========================================
    // UI
    // ========================================

    return (
        <div>

            <h1>Login</h1>

            <form onSubmit={handleLogin}>

                {/* =========================
                    EMAIL
                ========================= */}

                <div>

                    <label>
                        Email
                    </label>

                    <input
                        type="email"
                        value={email}
                        onChange={(e) =>
                            setEmail(e.target.value)
                        }
                        required
                    />

                </div>


                {/* =========================
                    PASSWORD
                ========================= */}

                <div>

                    <label>
                        Password
                    </label>

                    <input
                        type="password"
                        value={password}
                        onChange={(e) =>
                            setPassword(e.target.value)
                        }
                        required
                    />

                </div>


                {/* =========================
                    ERROR
                ========================= */}

                {error && (

                    <p style={{ color: "red" }}>
                        {error}
                    </p>

                )}


                {/* =========================
                    LOGIN BUTTON
                ========================= */}

                <button
                    type="submit"
                    disabled={loading}
                >

                    {loading
                        ? "Logging in..."
                        : "Login"}

                </button>

            </form>

        </div>
    );
}

export default Login;