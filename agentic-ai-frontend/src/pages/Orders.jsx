// NEXT MODULE: ORDERS
// src/pages/Orders.jsx

import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
    getMyOrders,
    cancelOrder
} from "../services/orderService";

function Orders() {

    const navigate = useNavigate();

    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // ========================================
    // LOAD ORDERS
    // ========================================

    const loadOrders = async () => {

        try {

            setLoading(true);
            setError("");

            const data = await getMyOrders();

            setOrders(data);

        } catch (error) {

            console.error(
                "Failed to load orders:",
                error
            );

            setError(
                error.response?.data?.message ||
                "Failed to load orders."
            );

        } finally {

            setLoading(false);
        }
    };


    // ========================================
    // INITIAL LOAD
    // ========================================

    useEffect(() => {

        loadOrders();

    }, []);


    // ========================================
    // CANCEL ORDER
    // ========================================

    const handleCancelOrder = async (orderId) => {

        const confirmed = window.confirm(
            "Are you sure you want to cancel this order?"
        );

        if (!confirmed) {
            return;
        }

        try {

            setError("");

            await cancelOrder(orderId);

            await loadOrders();

        } catch (error) {

            console.error(
                "Failed to cancel order:",
                error
            );

            setError(
                error.response?.data?.message ||
                "Failed to cancel order."
            );
        }
    };


    // ========================================
    // LOADING
    // ========================================

    if (loading) {

        return (
            <div>

                <h1>
                    My Orders
                </h1>

                <p>
                    Loading orders...
                </p>

            </div>
        );
    }


    // ========================================
    // UI
    // ========================================

    return (
        <div className="orders-page">

            <h1>
                My Orders
            </h1>


            {/* ERROR */}

            {error && (

                <p style={{ color: "red" }}>
                    {error}
                </p>

            )}


            {/* EMPTY ORDERS */}

            {orders.length === 0 ? (

                <div>

                    <p>
                        You have not placed any orders yet.
                    </p>

                    <button
                        onClick={() =>
                            navigate("/products")
                        }
                    >
                        Start Shopping
                    </button>

                </div>

            ) : (

                <div className="orders-list">

                    {orders.map((order) => (

                        <div
                            className="order-card"
                            key={order.orderId}
                        >

                            {/* ========================================
                                ORDER INFORMATION
                            ======================================== */}

                            <h2>
                                Order #{order.orderId}
                            </h2>

                            <p>
                                Date:{" "}
                                {order.orderDate
                                    ? new Date(
                                        order.orderDate
                                    ).toLocaleString()
                                    : "N/A"}
                            </p>

                            <p>
                                Total Amount: ₹
                                {order.totalAmount}
                            </p>

                            <p>
                                Status:{" "}
                                {order.status}
                            </p>


                            {/* ========================================
                                ORDER ITEMS
                            ======================================== */}

                            {order.items &&
                                order.items.length > 0 && (

                                    <div>

                                        <h3>
                                            Items
                                        </h3>

                                        {order.items.map(
                                            (item, index) => (

                                                <div
                                                    key={
                                                        item.orderItemId ||
                                                        index
                                                    }
                                                >

                                                    <p>
                                                        {item.productName}
                                                    </p>

                                                    <p>
                                                        Quantity:{" "}
                                                        {item.quantity}
                                                    </p>

                                                    <p>
                                                        Price: ₹
                                                        {item.price}
                                                    </p>

                                                </div>

                                            )
                                        )}

                                    </div>
                                )}


                            {/* ========================================
                                VIEW ORDER
                            ======================================== */}

                            <button
                                onClick={() =>
                                    navigate(
                                        `/orders/${order.orderId}`
                                    )
                                }
                            >
                                View Details
                            </button>


                            {/* ========================================
                                CANCEL ORDER
                            ======================================== */}

                            {order.status !== "CANCELLED" &&
                                order.status !== "DELIVERED" && (

                                    <button
                                        onClick={() =>
                                            handleCancelOrder(
                                                order.orderId
                                            )
                                        }
                                    >
                                        Cancel Order
                                    </button>

                                )}

                        </div>

                    ))}

                </div>

            )}

        </div>
    );
}

export default Orders;