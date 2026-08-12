import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../services/authService";

function Register() {

    const navigate = useNavigate();

    const [fullName, setFullName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);


    const handleRegister = async (e) => {

        e.preventDefault();

        setError("");
        setSuccess("");


        // ================================
        // PASSWORD CONFIRMATION
        // ================================

        if (password !== confirmPassword) {

            setError(
                "Password and Confirm Password do not match."
            );

            return;
        }


        // ================================
        // PASSWORD VALIDATION
        // ================================

        const passwordPattern =
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;

        if (!passwordPattern.test(password)) {

            setError(
                "Password must contain 8-20 characters, one uppercase letter, one lowercase letter, one digit, and one special character."
            );

            return;
        }


        setLoading(true);


        try {

            const response = await register(
                fullName,
                email,
                password
            );

            console.log(
                "Registration Response:",
                response
            );


            setSuccess(
                "Registration successful! Redirecting to login..."
            );


            // Redirect to login after 1.5 seconds
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


    return (
        <div>

            <h1>Register</h1>

            <form onSubmit={handleRegister}>

                {/* =========================
                    FULL NAME
                ========================== */}

                <div>

                    <label>
                        Full Name
                    </label>

                    <input
                        type="text"
                        value={fullName}
                        onChange={(e) =>
                            setFullName(e.target.value)
                        }
                        placeholder="Enter your full name"
                        minLength={3}
                        maxLength={50}
                        required
                    />

                </div>


                {/* =========================
                    EMAIL
                ========================== */}

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
                        placeholder="Enter your email"
                        required
                    />

                </div>


                {/* =========================
                    PASSWORD
                ========================== */}

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
                        placeholder="Enter your password"
                        minLength={8}
                        maxLength={20}
                        required
                    />

                </div>


                {/* =========================
                    CONFIRM PASSWORD
                ========================== */}

                <div>

                    <label>
                        Confirm Password
                    </label>

                    <input
                        type="password"
                        value={confirmPassword}
                        onChange={(e) =>
                            setConfirmPassword(e.target.value)
                        }
                        placeholder="Confirm your password"
                        required
                    />

                </div>


                {/* =========================
                    ERROR
                ========================== */}

                {error && (
                    <p style={{ color: "red" }}>
                        {error}
                    </p>
                )}


                {/* =========================
                    SUCCESS
                ========================== */}

                {success && (
                    <p style={{ color: "green" }}>
                        {success}
                    </p>
                )}


                {/* =========================
                    REGISTER BUTTON
                ========================== */}

                <button
                    type="submit"
                    disabled={loading}
                >

                    {loading
                        ? "Creating Account..."
                        : "Register"}

                </button>

            </form>


            {/* =========================
                LOGIN LINK
            ========================== */}

            <p>

                Already have an account?{" "}

                <button
                    type="button"
                    onClick={() => navigate("/login")}
                >
                    Login
                </button>

            </p>

        </div>
    );
}

export default Register;