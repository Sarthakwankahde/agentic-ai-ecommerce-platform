import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import {
    getOrderById,
    cancelOrder
} from "../services/orderService";

function OrderDetails() {

    const { orderId } = useParams();
    const navigate = useNavigate();

    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const email = localStorage.getItem("email");


    // ========================================
    // LOAD ORDER
    // ========================================

    const loadOrder = async () => {

        if (!email) {

            setError("User email not found.");
            setLoading(false);

            return;
        }

        try {

            setLoading(true);
            setError("");

            const data = await getOrderById(
                orderId,
                email
            );

            setOrder(data);

        } catch (error) {

            console.error(
                "Failed to load order:",
                error
            );

            setError(
                "Failed to load order."
            );

        } finally {

            setLoading(false);
        }
    };


    // ========================================
    // INITIAL LOAD
    // ========================================

    useEffect(() => {

        loadOrder();

    }, [orderId]);


    // ========================================
    // CANCEL ORDER
    // ========================================

    const handleCancelOrder = async () => {

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

            await loadOrder();

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

                <h1>
                    Order Details
                </h1>

                <p>
                    Loading order...
                </p>

            </div>
        );
    }


    // ========================================
    // ERROR
    // ========================================

    if (error || !order) {

        return (
            <div>

                <h1>
                    Order Details
                </h1>

                <p>
                    {error || "Order not found."}
                </p>

                <button
                    onClick={() =>
                        navigate("/orders")
                    }
                >
                    Back to Orders
                </button>

            </div>
        );
    }


    // ========================================
    // UI
    // ========================================

    return (

        <div className="order-details-page">

            {/* =========================
                HEADER
            ========================= */}

            <div>

                <button
                    onClick={() =>
                        navigate("/orders")
                    }
                >
                    ← Back to Orders
                </button>

                <h1>
                    Order #{order.orderId}
                </h1>

                <p>
                    Date:{" "}
                    {new Date(
                        order.orderDate
                    ).toLocaleString()}
                </p>

                <p>
                    Status:{" "}
                    <strong>
                        {order.status}
                    </strong>
                </p>

            </div>


            {/* =========================
                ORDER ITEMS
            ========================= */}

            <div>

                <h2>
                    Order Items
                </h2>

                {order.items &&
                    order.items.map((item) => (

                        <div
                            className="order-item"
                            key={item.productId}
                        >

                            <h3>
                                {item.productName}
                            </h3>

                            <p>
                                Product ID:{" "}
                                {item.productId}
                            </p>

                            <p>
                                Quantity:{" "}
                                {item.quantity}
                            </p>

                            <p>
                                Price: ₹
                                {item.price}
                            </p>

                            <p>
                                Item Total: ₹
                                {
                                    item.price *
                                    item.quantity
                                }
                            </p>

                        </div>

                    ))}

            </div>


            {/* =========================
                ORDER SUMMARY
            ========================= */}

            <div>

                <h2>
                    Order Summary
                </h2>

                <h3>
                    Total Amount: ₹
                    {order.totalAmount}
                </h3>

            </div>


            {/* =========================
                ACTIONS
            ========================= */}

            <div>

                {order.status !== "CANCELLED" &&
                    order.status !== "DELIVERED" && (

                        <button
                            onClick={
                                handleCancelOrder
                            }
                        >
                            Cancel Order
                        </button>

                    )}

                <button
                    onClick={() =>
                        navigate("/products")
                    }
                >
                    Continue Shopping
                </button>

            </div>

        </div>
    );
}

export default OrderDetails;