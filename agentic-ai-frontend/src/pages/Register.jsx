import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { register } from "../services/authService";
import heroImage from "../assets/hero.png";

function Register() {

    const navigate = useNavigate();

    const [fullName, setFullName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);


    // ========================================
    // HANDLE REGISTER
    // ========================================

    const handleRegister = async (e) => {

        e.preventDefault();

        setError("");
        setSuccess("");

        // ========================================
        // VALIDATE FULL NAME
        // ========================================

        if (fullName.trim().length < 3) {

            setError(
                "Full name must contain at least 3 characters."
            );

            return;
        }


        // ========================================
        // VALIDATE PASSWORD MATCH
        // ========================================

        if (password !== confirmPassword) {

            setError(
                "Password and Confirm Password do not match."
            );

            return;
        }


        // ========================================
        // PASSWORD VALIDATION
        // ========================================

        const passwordPattern =
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;


        if (!passwordPattern.test(password)) {

            setError(
                "Password must contain 8-20 characters, one uppercase letter, one lowercase letter, one digit, and one special character."
            );

            return;
        }


        // ========================================
        // START LOADING
        // ========================================

        setLoading(true);


        try {

            // ========================================
            // CALL REGISTER API
            // ========================================

            await register(
                fullName.trim(),
                email.trim(),
                password
            );


            // ========================================
            // SUCCESS
            // ========================================

            setSuccess(
                "Registration successful! Redirecting to login..."
            );


            setTimeout(() => {

                navigate("/login");

            }, 1500);


        } catch (error) {

            console.error(
                "Registration Error:",
                error
            );


            if (error.response) {

                setError(
                    error.response.data?.message ||
                    "Registration failed. Please try again."
                );

            } else {

                setError(
                    "Unable to connect to server."
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

        <div className="auth-page">

            <div className="auth-card register-card">

                {/* ========================================
                    LOGO
                ======================================== */}

                <div className="auth-logo">

                    <img
                        className="auth-logo"
                        src={heroImage}
                        alt="Agentic AI E-Commerce"
                    />

                </div>


                {/* ========================================
                    HEADER
                ======================================== */}

                <div className="auth-header">

                    <h1>
                        Create Account
                    </h1>

                    <p>
                        Join Agentic AI E-Commerce
                    </p>

                </div>


                {/* ========================================
                    REGISTER FORM
                ======================================== */}

                <form
                    className="auth-form"
                    onSubmit={handleRegister}
                >

                    {/* FULL NAME */}

                    <div className="form-group">

                        <label htmlFor="fullName">
                            Full Name
                        </label>

                        <input
                            id="fullName"
                            type="text"
                            value={fullName}
                            onChange={(e) =>
                                setFullName(e.target.value)
                            }
                            placeholder="Enter your full name"
                            minLength={3}
                            maxLength={50}
                            autoComplete="name"
                            required
                        />

                    </div>


                    {/* EMAIL */}

                    <div className="form-group">

                        <label htmlFor="email">
                            Email
                        </label>

                        <input
                            id="email"
                            type="email"
                            value={email}
                            onChange={(e) =>
                                setEmail(e.target.value)
                            }
                            placeholder="Enter your email"
                            autoComplete="email"
                            required
                        />

                    </div>


                    {/* PASSWORD */}

                    <div className="form-group">

                        <label htmlFor="password">
                            Password
                        </label>

                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(e) =>
                                setPassword(e.target.value)
                            }
                            placeholder="Enter your password"
                            minLength={8}
                            maxLength={20}
                            autoComplete="new-password"
                            required
                        />

                        <small className="input-help">
                            8-20 characters with uppercase,
                            lowercase, number and special character.
                        </small>

                    </div>


                    {/* CONFIRM PASSWORD */}

                    <div className="form-group">

                        <label htmlFor="confirmPassword">
                            Confirm Password
                        </label>

                        <input
                            id="confirmPassword"
                            type="password"
                            value={confirmPassword}
                            onChange={(e) =>
                                setConfirmPassword(e.target.value)
                            }
                            placeholder="Confirm your password"
                            autoComplete="new-password"
                            required
                        />

                    </div>


                    {/* ERROR */}

                    {error && (

                        <div className="auth-error">
                            {error}
                        </div>

                    )}


                    {/* SUCCESS */}

                    {success && (

                        <div className="auth-success">
                            {success}
                        </div>

                    )}


                    {/* REGISTER BUTTON */}

                    <button
                        className="auth-button"
                        type="submit"
                        disabled={loading}
                    >

                        {loading
                            ? "Creating Account..."
                            : "Create Account"}

                    </button>

                </form>


                {/* LOGIN LINK */}

                <div className="auth-footer">

                    <p>

                        Already have an account?{" "}

                        <Link to="/login">
                            Login
                        </Link>

                    </p>

                </div>

            </div>

        </div>
    );
}

export default Register;