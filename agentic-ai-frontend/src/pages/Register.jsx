import {
    useState
} from "react";

import {
    Link,
    useNavigate
} from "react-router-dom";

import {
    register
} from "../services/authService";


function Register() {

    const navigate = useNavigate();

    const [
        fullName,
        setFullName
    ] = useState("");

    const [
        email,
        setEmail
    ] = useState("");

    const [
        password,
        setPassword
    ] = useState("");

    const [
        confirmPassword,
        setConfirmPassword
    ] = useState("");

    const [
        error,
        setError
    ] = useState("");

    const [
        success,
        setSuccess
    ] = useState("");

    const [
        loading,
        setLoading
    ] = useState(false);


    const handleRegister = async (e) => {

        e.preventDefault();

        setError("");

        setSuccess("");


        if (
            fullName.trim().length < 3
        ) {

            setError(
                "Full name must contain at least 3 characters."
            );

            return;
        }


        if (
            password !== confirmPassword
        ) {

            setError(
                "Password and Confirm Password do not match."
            );

            return;
        }


        const passwordPattern =
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;


        if (
            !passwordPattern.test(password)
        ) {

            setError(
                "Password must contain 8-20 characters, one uppercase letter, one lowercase letter, one digit, and one special character."
            );

            return;
        }


        setLoading(true);


        try {

            await register(
                fullName.trim(),
                email.trim(),
                password
            );

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


    return (

        <div>

            <h1>
                Create Account
            </h1>

            <form
                onSubmit={handleRegister}
            >

                <div>

                    <label>
                        Full Name
                    </label>

                    <input
                        type="text"
                        value={fullName}
                        onChange={(e) =>
                            setFullName(
                                e.target.value
                            )
                        }
                        placeholder="Enter your full name"
                        minLength={3}
                        maxLength={50}
                        autoComplete="name"
                        required
                    />

                </div>


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
                        minLength={8}
                        maxLength={20}
                        autoComplete="new-password"
                        required
                    />

                </div>


                <div>

                    <label>
                        Confirm Password
                    </label>

                    <input
                        type="password"
                        value={confirmPassword}
                        onChange={(e) =>
                            setConfirmPassword(
                                e.target.value
                            )
                        }
                        placeholder="Confirm your password"
                        autoComplete="new-password"
                        required
                    />

                </div>


                {error && (

                    <p>
                        {error}
                    </p>

                )}


                {success && (

                    <p>
                        {success}
                    </p>

                )}


                <button
                    type="submit"
                    disabled={loading}
                >

                    {loading
                        ? "Creating Account..."
                        : "Register"}

                </button>

            </form>


            <p>

                Already have an account?{" "}

                <Link to="/login">
                    Login
                </Link>

            </p>

        </div>
    );
}

export default Register;