// src/pages/OrderDetails.jsx

import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { getOrderById } from "../services/orderService";

function OrderDetails() {

    const { orderId } = useParams();
    const navigate = useNavigate();

    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");


    // ========================================
    // LOAD ORDER
    // ========================================

    useEffect(() => {

        const loadOrder = async () => {

            try {

                setLoading(true);
                setError("");

                const data =
                    await getOrderById(orderId);

                setOrder(data);

            } catch (error) {

                console.error(
                    "Failed to load order:",
                    error
                );

                setError(
                    error.response?.data?.message ||
                    "Failed to load order."
                );

            } finally {

                setLoading(false);
            }
        };

        loadOrder();

    }, [orderId]);


    // ========================================
    // LOADING
    // ========================================

    if (loading) {

        return (
            <div>
                <h1>Order Details</h1>
                <p>Loading order...</p>
            </div>
        );
    }


    // ========================================
    // ERROR
    // ========================================

    if (error || !order) {

        return (
            <div>

                <h1>Order Details</h1>

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

            <button
                onClick={() =>
                    navigate("/orders")
                }
            >
                ← Back to Orders
            </button>


            <h1>
                Order #{order.id}
            </h1>


            <p>
                Status: {order.status}
            </p>

            <p>
                Total Amount: ₹
                {order.totalAmount}
            </p>

            <p>
                Order Date: {order.orderDate}
            </p>


            {/* ========================================
                ORDER ITEMS
            ======================================== */}

            <h2>
                Order Items
            </h2>

            <div>

                {order.items?.map((item) => (

                    <div
                        key={item.id}
                        className="order-item"
                    >

                        <h3>
                            {item.productName}
                        </h3>

                        <p>
                            Price: ₹{item.price}
                        </p>

                        <p>
                            Quantity: {item.quantity}
                        </p>

                        <p>
                            Total: ₹{item.totalPrice}
                        </p>

                    </div>

                ))}

            </div>

        </div>
    );
}

export default OrderDetails;