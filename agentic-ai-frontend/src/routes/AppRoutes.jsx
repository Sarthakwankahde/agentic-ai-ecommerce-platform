
import {
    Routes,
    Route
} from "react-router-dom";

import Navbar from "../components/Navbar";

import Home from "../pages/Home";
import Login from "../pages/Login";
import Register from "../pages/Register";
import ForgotPassword from "../pages/ForgotPassword";

import Products from "../pages/Products";
import ProductDetails from "../pages/ProductDetails";

import Cart from "../pages/Cart";
import Wishlist from "../pages/Wishlist";

import Orders from "../pages/Orders";
import OrderDetails from "../pages/OrderDetails";

import Profile from "../pages/Profile";
import Address from "../pages/Address";

import ProtectedRoute
    from "./ProtectedRoute";


function AppRoutes() {

    return (

        <>

            <Navbar />

            <Routes>

                {/* =========================
                    PUBLIC
                ========================= */}

                <Route
                    path="/"
                    element={<Home />}
                />

                <Route
                    path="/login"
                    element={<Login />}
                />

                <Route
                    path="/register"
                    element={<Register />}
                />

                <Route
                    path="/forgot-password"
                    element={<ForgotPassword />}
                />

                <Route
                    path="/products"
                    element={<Products />}
                />

                <Route
                    path="/products/:id"
                    element={
                        <ProductDetails />
                    }
                />


                {/* =========================
                    PROTECTED
                ========================= */}

                <Route
                    element={
                        <ProtectedRoute />
                    }
                >

                    <Route
                        path="/cart"
                        element={<Cart />}
                    />

                    <Route
                        path="/wishlist"
                        element={<Wishlist />}
                    />

                    <Route
                        path="/orders"
                        element={<Orders />}
                    />

                    <Route
                        path="/orders/:orderId"
                        element={
                            <OrderDetails />
                        }
                    />

                    <Route
                        path="/profile"
                        element={<Profile />}
                    />

                    <Route
                        path="/addresses"
                        element={<Address />}
                    />

                </Route>


                {/* =========================
                    FALLBACK
                ========================= */}

                <Route
                    path="*"
                    element={
                        <h1>
                            404 - Page Not Found
                        </h1>
                    }
                />

            </Routes>

        </>

    );
}

export default AppRoutes;