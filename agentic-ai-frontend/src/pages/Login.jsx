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


function Login() {

    const navigate = useNavigate();

    const {
        login
    } = useAuth();

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

            if (!data.accessToken) {

                throw new Error(
                    "Access token not received"
                );
            }

            login(
                data.accessToken,
                data.refreshToken,
                data.user || null
            );

            localStorage.setItem(
                "email",
                email.trim()
            );

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


    return (

        <div>

            <h1>
                Login
            </h1>

            <form
                onSubmit={handleLogin}
            >

                <div>

                    <label>
                        Email
                    </label>

                    <input
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


                <div>

                    <label>
                        Password
                    </label>

                    <input
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


                {error && (

                    <p>
                        {error}
                    </p>

                )}


                <button
                    type="submit"
                    disabled={loading}
                >

                    {loading
                        ? "Logging in..."
                        : "Login"}

                </button>

            </form>


            <p>

                Don't have an account?{" "}

                <Link to="/register">
                    Register
                </Link>

            </p>


            <p>

                <Link to="/forgot-password">
                    Forgot Password?
                </Link>

            </p>

        </div>
    );
}

export default Login;