import {
    useState
} from "react";

import {
    Link,
    useNavigate
} from "react-router-dom";

import {
    login as loginApi
} from "../services/authService";

import {
    useAuth
} from "../context/AuthContext";
import heroImage from "../assets/hero.png";


function Login() {

    const navigate = useNavigate();

    const {
        login
    } = useAuth();


    // ========================================
    // STATE
    // ========================================

    const [
        email,
        setEmail
    ] = useState("");


    const [
        password,
        setPassword
    ] = useState("");


    const [
        error,
        setError
    ] = useState("");


    const [
        loading,
        setLoading
    ] = useState(false);


    // ========================================
    // HANDLE LOGIN
    // ========================================

    const handleLogin = async (e) => {

        e.preventDefault();

        setError("");

        setLoading(true);


        try {

            const data =
                await loginApi(
                    email.trim(),
                    password
                );


            // ========================================
            // CHECK ACCESS TOKEN
            // ========================================

            if (!data.accessToken) {

                throw new Error(
                    "Access token not received"
                );
            }


            // ========================================
            // SAVE AUTHENTICATION
            // ========================================

            login(
                data.accessToken,
                data.refreshToken,
                data.user || null
            );


            // ========================================
            // SAVE EMAIL
            // ========================================

            localStorage.setItem(
                "email",
                email.trim()
            );


            // ========================================
            // GO TO HOME
            // ========================================

            navigate("/");

        } catch (error) {

            console.error(
                "Login Error:",
                error
            );


            if (error.response) {

                setError(
                    error.response.data?.message ||
                    "Invalid email or password."
                );

            } else {

                setError(
                    error.message ===
                    "Access token not received"

                        ? "Login response did not contain an access token."

                        : "Unable to connect to server."
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

            <div className="auth-card">


                {/* ========================================
                    LOGO
                ======================================== */}
                <img
                    className="auth-logo"
                    src={heroImage}
                    alt="Agentic AI E-Commerce"
                />


                {/* ========================================
                    HEADER
                ======================================== */}

                <div className="auth-header">

                    <h1>
                        Welcome Back
                    </h1>

                    <p className="auth-subtitle">
                        Login to your account
                    </p>

                </div>


                {/* ========================================
                    LOGIN FORM
                ======================================== */}

                <form
                    className="auth-form"
                    onSubmit={handleLogin}
                >


                    {/* ========================================
                        EMAIL
                    ======================================== */}

                    <div className="form-group">

                        <label htmlFor="email">
                            Email
                        </label>

                        <input
                            id="email"
                            type="email"
                            value={email}
                            onChange={(e) =>
                                setEmail(
                                    e.target.value
                                )
                            }
                            placeholder="Enter your email"
                            autoComplete="email"
                            required
                        />

                    </div>


                    {/* ========================================
                        PASSWORD
                    ======================================== */}

                    <div className="form-group">

                        <label htmlFor="password">
                            Password
                        </label>

                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(e) =>
                                setPassword(
                                    e.target.value
                                )
                            }
                            placeholder="Enter your password"
                            autoComplete="current-password"
                            required
                        />

                    </div>


                    {/* ========================================
                        FORGOT PASSWORD
                    ======================================== */}

                    <div className="forgot-password">

                        <Link to="/forgot-password">
                            Forgot Password?
                        </Link>

                    </div>


                    {/* ========================================
                        ERROR MESSAGE
                    ======================================== */}

                    {error && (

                        <div className="auth-error">

                            {error}

                        </div>

                    )}


                    {/* ========================================
                        LOGIN BUTTON
                    ======================================== */}

                    <button
                        className="auth-button"
                        type="submit"
                        disabled={loading}
                    >

                        {loading
                            ? "Logging in..."
                            : "Login"}

                    </button>

                </form>


                {/* ========================================
                    REGISTER LINK
                ======================================== */}

                <div className="auth-links">

                    <p>

                        Don't have an account?{" "}

                        <Link to="/register">
                            Create Account
                        </Link>

                    </p>

                </div>


            </div>

        </div>
    );
}


export default Login;