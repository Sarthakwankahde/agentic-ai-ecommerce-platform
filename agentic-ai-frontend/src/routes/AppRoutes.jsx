// src/routes/AppRoutes.jsx

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
import Checkout from "../pages/Checkout";

import ProtectedRoute from "./ProtectedRoute";


function AppRoutes() {

    return (

        <>

            <Navbar />

            <Routes>

                {/* =========================
                    PUBLIC ROUTES
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
                    PROTECTED ROUTES
                ========================= */}

                <Route
                    element={
                        <ProtectedRoute />
                    }
                >

                    {/* CART */}

                    <Route
                        path="/cart"
                        element={<Cart />}
                    />


                    {/* CHECKOUT */}

                    <Route
                        path="/checkout"
                        element={<Checkout />}
                    />


                    {/* WISHLIST */}

                    <Route
                        path="/wishlist"
                        element={<Wishlist />}
                    />


                    {/* ORDERS */}

                    <Route
                        path="/orders"
                        element={<Orders />}
                    />


                    {/* ORDER DETAILS */}

                    <Route
                        path="/orders/:orderId"
                        element={
                            <OrderDetails />
                        }
                    />


                    {/* PROFILE */}

                    <Route
                        path="/profile"
                        element={<Profile />}
                    />


                    {/* ADDRESSES */}

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