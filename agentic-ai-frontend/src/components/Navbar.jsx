import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Navbar() {

    const { isAuthenticated, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    return (
        <nav className="navbar">

            <div className="navbar-brand">
                <Link to="/">
                    Agentic AI
                </Link>
            </div>

            <div className="navbar-links">

                <Link to="/">
                    Home
                </Link>

                <Link to="/products">
                    Products
                </Link>

                {isAuthenticated && (
                    <>
                        <Link to="/cart">
                            Cart
                        </Link>

                        <Link to="/wishlist">
                            Wishlist
                        </Link>

                        <Link to="/orders">
                            Orders
                        </Link>

                        <Link to="/profile">
                            Profile
                        </Link>
                    </>
                )}

            </div>

            <div className="navbar-auth">

                {!isAuthenticated ? (
                    <>
                        <Link to="/login">
                            Login
                        </Link>

                        <Link to="/register">
                            Register
                        </Link>
                    </>
                ) : (
                    <button onClick={handleLogout}>
                        Logout
                    </button>
                )}

            </div>

        </nav>
    );
}

export default Navbar;