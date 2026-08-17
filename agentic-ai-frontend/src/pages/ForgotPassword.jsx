// src/pages/ForgotPassword.jsx

import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { forgotPassword } from "../services/authService";

function ForgotPassword() {

    const navigate = useNavigate();

    const [email, setEmail] = useState("");

    const [loading, setLoading] =
        useState(false);

    const [message, setMessage] =
        useState("");

    const [error, setError] =
        useState("");


    // ========================================
    // HANDLE FORGOT PASSWORD
    // ========================================

    const handleSubmit = async (event) => {

        event.preventDefault();

        setMessage("");
        setError("");

        try {

            setLoading(true);

            const response =
                await forgotPassword(email);

            setMessage(response);

            setEmail("");

        } catch (error) {

            console.error(
                "Forgot password error:",
                error
            );

            setError(
                error.response?.data?.message ||
                error.response?.data ||
                "Failed to process password reset request."
            );

        } finally {

            setLoading(false);
        }
    };


    // ========================================
    // UI
    // ========================================

    return (

        <div className="forgot-password-page">

            <h1>
                Forgot Password
            </h1>

            <p>
                Enter your registered email address
                to reset your password.
            </p>


            {/* ========================================
                SUCCESS MESSAGE
            ======================================== */}

            {message && (

                <p style={{ color: "green" }}>
                    {message}
                </p>

            )}


            {/* ========================================
                ERROR MESSAGE
            ======================================== */}

            {error && (

                <p style={{ color: "red" }}>
                    {error}
                </p>

            )}


            {/* ========================================
                FORM
            ======================================== */}

            <form
                onSubmit={handleSubmit}
            >

                <div>

                    <label>
                        Email
                    </label>

                    <input
                        type="email"
                        value={email}
                        onChange={(event) =>
                            setEmail(
                                event.target.value
                            )
                        }
                        placeholder="Enter your email"
                        required
                    />

                </div>


                <button
                    type="submit"
                    disabled={loading}
                >

                    {loading
                        ? "Sending..."
                        : "Reset Password"}

                </button>

            </form>


            {/* ========================================
                BACK TO LOGIN
            ======================================== */}

            <button
                type="button"
                onClick={() =>
                    navigate("/login")
                }
            >
                Back to Login
            </button>

        </div>
    );
}

export default ForgotPassword;