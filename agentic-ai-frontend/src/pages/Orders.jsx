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

    const email = localStorage.getItem("email");


    // ========================================
    // LOAD ORDERS
    // ========================================

    const loadOrders = async () => {

        if (!email) {

            setError("User email not found.");
            setLoading(false);

            return;
        }

        try {

            setLoading(true);
            setError("");

            const data = await getMyOrders(email);

            setOrders(data);

        } catch (error) {

            console.error(
                "Failed to load orders:",
                error
            );

            setError(
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

            await cancelOrder(
                orderId,
                email
            );

            await loadOrders();

        } catch (error) {

            console.error(
                "Failed to cancel order:",
                error
            );

            setError(
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

                <h1>Orders</h1>

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


            {error && (
                <p>
                    {error}
                </p>
            )}


            {orders.length === 0 ? (

                <div>

                    <p>
                        You have no orders yet.
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

                            {/* =========================
                                ORDER HEADER
                            ========================= */}

                            <div>

                                <h2>
                                    Order #{order.orderId}
                                </h2>

                                <p>
                                    Date:{" "}
                                    {new Date(
                                        order.orderDate
                                    ).toLocaleString()}
                                </p>

                                <p>
                                    Status:{" "}
                                    {order.status}
                                </p>

                                <h3>
                                    Total: ₹
                                    {order.totalAmount}
                                </h3>

                            </div>


                            {/* =========================
                                ORDER ITEMS
                            ========================= */}

                            <div>

                                <h3>
                                    Items
                                </h3>

                                {order.items &&
                                    order.items.map(
                                        (item, index) => (

                                            <div
                                                key={item.productId}
                                            >

                                                <p>
                                                    Product:{" "}
                                                    {
                                                        item.productName
                                                    }
                                                </p>

                                                <p>
                                                    Quantity:{" "}
                                                    {
                                                        item.quantity
                                                    }
                                                </p>

                                                <p>
                                                    Price: ₹
                                                    {
                                                        item.price
                                                    }
                                                </p>

                                            </div>

                                        )
                                    )}

                            </div>


                            {/* =========================
                                ORDER ACTIONS
                            ========================= */}

                            <div>

                                <button
                                    onClick={() =>
                                        navigate(
                                            `/orders/${order.orderId}`
                                        )
                                    }
                                >
                                    View Details
                                </button>


                                {order.status !==
                                    "CANCELLED" &&
                                    order.status !==
                                    "DELIVERED" && (

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

                        </div>

                    ))}

                </div>

            )}

        </div>
    );
}

export default Orders;