// src/pages/OrderDetails.jsx

import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { getOrderById } from "../services/orderService";

import {
    createPaymentOrder,
    verifyPayment
} from "../services/paymentService";


function OrderDetails() {

    const { orderId } = useParams();
    const navigate = useNavigate();


    // ========================================
    // STATE
    // ========================================

    const [order, setOrder] = useState(null);

    const [loading, setLoading] =
        useState(true);

    const [paymentLoading, setPaymentLoading] =
        useState(false);

    const [error, setError] =
        useState("");

    const [message, setMessage] =
        useState("");

    const [paymentStatus, setPaymentStatus] =
        useState("");


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
    // HANDLE PAYMENT
    // ========================================

    const handlePayment = async () => {

        // Prevent duplicate payment requests
        if (paymentLoading) {
            return;
        }

        try {

            setPaymentLoading(true);

            setError("");
            setMessage("");
            setPaymentStatus("PROCESSING");


            // ========================================
            // STEP 1
            // CREATE RAZORPAY ORDER
            // ========================================

            const paymentOrder =
                await createPaymentOrder(
                    order.orderId
                );


            console.log(
                "Payment Order:",
                paymentOrder
            );


            // ========================================
            // CHECK RAZORPAY SCRIPT
            // ========================================

            if (!window.Razorpay) {

                throw new Error(
                    "Razorpay SDK is not loaded."
                );
            }


            // ========================================
            // STEP 2
            // RAZORPAY CHECKOUT OPTIONS
            // ========================================

            const options = {

                key:
                    import.meta.env
                        .VITE_RAZORPAY_KEY_ID,


                amount:
                    paymentOrder.amount * 100,


                currency: "INR",


                name:
                    "Agentic AI E-Commerce",


                description:
                    `Payment for Order #${order.orderId}`,


                order_id:
                    paymentOrder.razorpayOrderId,


                // ========================================
                // PAYMENT SUCCESS
                // ========================================

                handler: async function (
                    response
                ) {

                    try {

                        console.log(
                            "Razorpay Response:",
                            response
                        );


                        setPaymentLoading(true);

                        setError("");
                        setMessage("");

                        setPaymentStatus(
                            "PROCESSING"
                        );


                        // ========================================
                        // STEP 3
                        // VERIFY PAYMENT
                        // ========================================

                        const verificationResponse =
                            await verifyPayment(

                                response.razorpay_order_id,

                                response.razorpay_payment_id,

                                response.razorpay_signature

                            );


                        console.log(
                            "Payment Verification:",
                            verificationResponse
                        );


                        // ========================================
                        // PAYMENT VERIFIED
                        // ========================================

                        setPaymentStatus(
                            "SUCCESS"
                        );

                        setMessage(
                            "Payment successful!"
                        );


                        // ========================================
                        // RELOAD ORDER
                        // ========================================

                        const updatedOrder =
                            await getOrderById(
                                order.orderId
                            );


                        setOrder(
                            updatedOrder
                        );


                    } catch (error) {

                        console.error(
                            "Payment verification failed:",
                            error
                        );


                        setPaymentStatus(
                            "FAILED"
                        );


                        setError(
                            error.response?.data?.message ||
                            error.message ||
                            "Payment verification failed."
                        );

                    } finally {

                        setPaymentLoading(false);
                    }
                },


                // ========================================
                // PREFILL CUSTOMER INFORMATION
                // ========================================

                prefill: {

                    name:
                        localStorage.getItem(
                            "fullName"
                        ) || "",


                    email:
                        localStorage.getItem(
                            "email"
                        ) || ""

                },


                // ========================================
                // THEME
                // ========================================

                theme: {

                    color: "#3399cc"

                }

            };


            // ========================================
            // STEP 4
            // CREATE RAZORPAY INSTANCE
            // ========================================

            const razorpay =
                new window.Razorpay(
                    options
                );


            // ========================================
            // PAYMENT FAILED
            // ========================================

            razorpay.on(
                "payment.failed",
                function (response) {

                    console.error(
                        "Payment failed:",
                        response
                    );


                    setPaymentStatus(
                        "FAILED"
                    );


                    setError(
                        response.error?.description ||
                        "Payment failed."
                    );


                    setPaymentLoading(false);
                }
            );


            // ========================================
            // OPEN RAZORPAY CHECKOUT
            // ========================================

            razorpay.open();

        } catch (error) {

            console.error(
                "Payment error:",
                error
            );


            setPaymentStatus(
                "FAILED"
            );


            setError(
                error.response?.data?.message ||
                error.message ||
                "Failed to initiate payment."
            );


            setPaymentLoading(false);
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
    // ERROR WHILE LOADING ORDER
    // ========================================

    if (error && !order) {

        return (
            <div>

                <h1>
                    Order Details
                </h1>

                <p>
                    {error}
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
    // ORDER NOT FOUND
    // ========================================

    if (!order) {

        return (
            <div>

                <h1>
                    Order Details
                </h1>

                <p>
                    Order not found.
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


            {/* ========================================
                BACK TO ORDERS
            ======================================== */}

            <button
                onClick={() =>
                    navigate("/orders")
                }
            >
                ← Back to Orders
            </button>


            {/* ========================================
                ORDER INFORMATION
            ======================================== */}

            <h1>
                Order #{order.orderId}
            </h1>


            <p>
                Status: {order.status}
            </p>


            <p>
                Total Amount: ₹
                {order.totalAmount}
            </p>


            <p>
                Order Date:{" "}

                {order.orderDate
                    ? new Date(
                        order.orderDate
                    ).toLocaleString()
                    : "N/A"}

            </p>


            {/* ========================================
                PAYMENT SUCCESS
            ======================================== */}

            {paymentStatus === "SUCCESS" && (

                <p
                    style={{
                        color: "green"
                    }}
                >
                    ✓ Payment Successful
                </p>

            )}


            {/* ========================================
                PAYMENT PROCESSING
            ======================================== */}

            {paymentStatus === "PROCESSING" && (

                <p>
                    Payment is being processed...
                </p>

            )}


            {/* ========================================
                PAYMENT FAILED
            ======================================== */}

            {paymentStatus === "FAILED" && (

                <p
                    style={{
                        color: "red"
                    }}
                >
                    ✗ Payment Failed
                </p>

            )}


            {/* ========================================
                SUCCESS MESSAGE
            ======================================== */}

            {message && (

                <p
                    style={{
                        color: "green"
                    }}
                >
                    {message}
                </p>

            )}


            {/* ========================================
                ERROR MESSAGE
            ======================================== */}

            {error && (

                <p
                    style={{
                        color: "red"
                    }}
                >
                    {error}
                </p>

            )}


            {/* ========================================
                PAYMENT BUTTON
            ======================================== */}

            {order.status !== "CANCELLED" &&
                order.status !== "DELIVERED" &&
                paymentStatus !== "SUCCESS" && (

                    <button
                        onClick={handlePayment}
                        disabled={paymentLoading}
                    >

                        {paymentLoading
                            ? "Processing Payment..."
                            : "Pay Now"}

                    </button>

                )}


            {/* ========================================
                ORDER ITEMS
            ======================================== */}

            <h2>
                Order Items
            </h2>


            <div>

                {order.items?.length > 0 ? (

                    order.items.map((item) => (

                        <div
                            key={item.productId}
                            className="order-item"
                        >

                            <h3>
                                {item.productName}
                            </h3>


                            <p>
                                Product ID:{" "}
                                {item.productId}
                            </p>


                            <p>
                                Price: ₹
                                {item.price}
                            </p>


                            <p>
                                Quantity:{" "}
                                {item.quantity}
                            </p>


                            <p>
                                Item Total: ₹
                                {(
                                    item.price *
                                    item.quantity
                                ).toFixed(2)}
                            </p>

                        </div>

                    ))

                ) : (

                    <p>
                        No items found in this order.
                    </p>

                )}

            </div>

        </div>
    );
}


export default OrderDetails;