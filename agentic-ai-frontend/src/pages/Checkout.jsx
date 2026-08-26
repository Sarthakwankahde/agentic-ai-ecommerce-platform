// src/pages/Checkout.jsx

import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getMyAddresses } from "../services/addressService";

import { placeOrder } from "../services/orderService";

import {
    createPaymentOrder,
    verifyPayment
} from "../services/paymentService";


function Checkout() {

    const navigate = useNavigate();

    const email =
        localStorage.getItem("email");


    // ========================================
    // STATE
    // ========================================

    const [addresses, setAddresses] =
        useState([]);

    const [selectedAddressId, setSelectedAddressId] =
        useState(null);

    const [loading, setLoading] =
        useState(true);

    const [orderLoading, setOrderLoading] =
        useState(false);

    const [error, setError] =
        useState("");


    // ========================================
    // LOAD ADDRESSES
    // ========================================

    const loadAddresses = async () => {

        if (!email) {

            setError(
                "User email not found. Please login again."
            );

            setLoading(false);

            return;
        }

        try {

            setLoading(true);
            setError("");

            const data =
                await getMyAddresses(email);

            setAddresses(data);


            // ========================================
            // SELECT DEFAULT ADDRESS
            // ========================================

            const defaultAddress =
                data.find(
                    (address) =>
                        address.isDefault === true
                );


            if (defaultAddress) {

                setSelectedAddressId(
                    defaultAddress.id
                );

            } else if (data.length > 0) {

                setSelectedAddressId(
                    data[0].id
                );
            }

        } catch (error) {

            console.error(
                "Failed to load addresses:",
                error
            );

            setError(
                error.response?.data?.message ||
                "Failed to load addresses."
            );

        } finally {

            setLoading(false);
        }
    };


    // ========================================
    // INITIAL LOAD
    // ========================================

    useEffect(() => {

        loadAddresses();

    }, []);


    // ========================================
    // LOAD RAZORPAY SCRIPT
    // ========================================

    const loadRazorpayScript = () => {

        return new Promise((resolve) => {

            if (
                window.Razorpay
            ) {

                resolve(true);

                return;
            }


            const existingScript =
                document.querySelector(
                    'script[src="https://checkout.razorpay.com/v1/checkout.js"]'
                );


            if (existingScript) {

                existingScript.addEventListener(
                    "load",
                    () => resolve(true)
                );

                existingScript.addEventListener(
                    "error",
                    () => resolve(false)
                );

                return;
            }


            const script =
                document.createElement("script");

            script.src =
                "https://checkout.razorpay.com/v1/checkout.js";

            script.onload = () => {

                resolve(true);

            };

            script.onerror = () => {

                resolve(false);

            };

            document.body.appendChild(script);
        });
    };


    // ========================================
    // PLACE ORDER + PAYMENT
    // ========================================

    const handlePlaceOrder = async () => {

        if (!email) {

            setError(
                "User email not found. Please login again."
            );

            return;
        }


        if (addresses.length === 0) {

            setError(
                "Please add a delivery address before placing the order."
            );

            return;
        }


        if (!selectedAddressId) {

            setError(
                "Please select a delivery address."
            );

            return;
        }


        try {

            setOrderLoading(true);
            setError("");


            // ========================================
            // STEP 1
            // CREATE APPLICATION ORDER
            // ========================================

            const order =
                await placeOrder();


            console.log(
                "Order created:",
                order
            );


            const orderId =
                order.orderId;


            if (!orderId) {

                throw new Error(
                    "Order ID was not returned by the server."
                );
            }


            // ========================================
            // STEP 2
            // LOAD RAZORPAY
            // ========================================

            const razorpayLoaded =
                await loadRazorpayScript();


            if (!razorpayLoaded) {

                throw new Error(
                    "Razorpay SDK failed to load."
                );
            }


            // ========================================
            // STEP 3
            // CREATE RAZORPAY ORDER
            // ========================================

            const payment =
                await createPaymentOrder(
                    orderId
                );


            console.log(
                "Payment order created:",
                payment
            );


            // ========================================
            // STEP 4
            // GET RAZORPAY KEY
            // ========================================

            const razorpayKey =
                import.meta.env
                    .VITE_RAZORPAY_KEY_ID;


            if (!razorpayKey) {

                throw new Error(
                    "Razorpay Key ID is not configured."
                );
            }


            // ========================================
            // STEP 5
            // RAZORPAY CHECKOUT OPTIONS
            // ========================================

            const options = {

                key: razorpayKey,

                amount:
                    Math.round(
                        Number(payment.amount) * 100
                    ),

                currency: "INR",

                name:
                    "Agentic AI E-Commerce",

                description:
                    `Payment for Order #${orderId}`,

                order_id:
                payment.razorpayOrderId,


                // ========================================
                // PREFILL
                // ========================================

                prefill: {

                    email: email || ""
                },


                // ========================================
                // PAYMENT SUCCESS
                // ========================================

                handler:
                    async function (
                        response
                    ) {

                        try {

                            setOrderLoading(true);
                            setError("");


                            // ========================================
                            // STEP 6
                            // VERIFY PAYMENT
                            // ========================================

                            const verifiedPayment =
                                await verifyPayment(

                                    response.razorpay_order_id,

                                    response.razorpay_payment_id,

                                    response.razorpay_signature
                                );


                            console.log(
                                "Payment verified:",
                                verifiedPayment
                            );


                            alert(
                                "Payment successful!"
                            );


                            // ========================================
                            // STEP 7
                            // ORDER DETAILS
                            // ========================================

                            navigate(
                                `/orders/${orderId}`
                            );

                        } catch (error) {

                            console.error(
                                "Payment verification failed:",
                                error
                            );

                            setError(
                                error.response?.data?.message ||
                                error.message ||
                                "Payment verification failed."
                            );

                        } finally {

                            setOrderLoading(false);
                        }
                    },


                // ========================================
                // PAYMENT FAILED
                // ========================================

                modal: {

                    ondismiss: function () {

                        setOrderLoading(false);

                    }
                },


                theme: {

                    color: "#3399cc"

                }
            };


            // ========================================
            // CREATE RAZORPAY INSTANCE
            // ========================================

            const razorpay =
                new window.Razorpay(
                    options
                );


            // ========================================
            // PAYMENT FAILED EVENT
            // ========================================

            razorpay.on(
                "payment.failed",
                function (response) {

                    console.error(
                        "Payment failed:",
                        response.error
                    );

                    setError(
                        response.error?.description ||
                        "Payment failed."
                    );

                    setOrderLoading(false);
                }
            );


            // ========================================
            // OPEN RAZORPAY
            // ========================================

            razorpay.open();


        } catch (error) {

            console.error(
                "Failed to place order:",
                error
            );

            setError(
                error.response?.data?.message ||
                error.message ||
                "Failed to place order."
            );

            setOrderLoading(false);
        }
    };


    // ========================================
    // LOADING
    // ========================================

    if (loading) {

        return (

            <div className="checkout-page">

                <h1>
                    Checkout
                </h1>

                <p>
                    Loading checkout...
                </p>

            </div>
        );
    }


    // ========================================
    // UI
    // ========================================

    return (

        <div className="checkout-page">

            <h1>
                Checkout
            </h1>


            {/* ========================================
                ERROR
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
                DELIVERY ADDRESS
            ======================================== */}

            <section>

                <h2>
                    Delivery Address
                </h2>


                {addresses.length === 0 ? (

                    <div>

                        <p>
                            You don't have any saved addresses.
                        </p>

                        <button
                            type="button"
                            onClick={() =>
                                navigate(
                                    "/addresses"
                                )
                            }
                        >
                            Add Address
                        </button>

                    </div>

                ) : (

                    <div>

                        {addresses.map(
                            (address) => (

                                <div
                                    key={address.id}
                                    style={{
                                        border:
                                            selectedAddressId === address.id
                                                ? "2px solid #3399cc"
                                                : "1px solid #ccc",
                                        padding: "15px",
                                        marginBottom: "10px"
                                    }}
                                >

                                    <label>

                                        <input
                                            type="radio"
                                            name="address"
                                            value={address.id}
                                            checked={
                                                selectedAddressId ===
                                                address.id
                                            }
                                            onChange={() =>
                                                setSelectedAddressId(
                                                    address.id
                                                )
                                            }
                                        />

                                        {" "}

                                        <strong>
                                            {
                                                address.fullName
                                            }
                                        </strong>

                                        {address.isDefault && (

                                            <span>
                                                {" "}
                                                (Default)
                                            </span>

                                        )}

                                    </label>


                                    <p>
                                        {
                                            address.addressLine1
                                        }
                                    </p>


                                    {address.addressLine2 && (

                                        <p>
                                            {
                                                address.addressLine2
                                            }
                                        </p>

                                    )}


                                    <p>
                                        {
                                            address.city
                                        }
                                        ,{" "}
                                        {
                                            address.state
                                        }
                                        ,{" "}
                                        {
                                            address.country
                                        }
                                    </p>


                                    <p>
                                        Pincode:{" "}
                                        {
                                            address.pincode
                                        }
                                    </p>


                                    <p>
                                        Mobile:{" "}
                                        {
                                            address.mobileNumber
                                        }
                                    </p>

                                </div>

                            )
                        )}

                    </div>

                )}

            </section>


            {/* ========================================
                CHECKOUT SUMMARY
            ======================================== */}

            <section>

                <h2>
                    Order Summary
                </h2>

                <p>
                    Your current cart items will be
                    converted into an order.
                </p>

                <p>
                    Selected Address ID:{" "}
                    {
                        selectedAddressId ||
                        "None"
                    }
                </p>

            </section>


            {/* ========================================
                PLACE ORDER + PAYMENT
            ======================================== */}

            <button
                type="button"
                onClick={handlePlaceOrder}
                disabled={
                    orderLoading ||
                    addresses.length === 0 ||
                    !selectedAddressId
                }
            >

                {orderLoading
                    ? "Processing Payment..."
                    : "Place Order & Pay"}

            </button>


            {/* ========================================
                MANAGE ADDRESSES
            ======================================== */}

            <button
                type="button"
                onClick={() =>
                    navigate(
                        "/addresses"
                    )
                }
                disabled={orderLoading}
            >
                Manage Addresses
            </button>


            {/* ========================================
                BACK TO CART
            ======================================== */}

            <button
                type="button"
                onClick={() =>
                    navigate(
                        "/cart"
                    )
                }
                disabled={orderLoading}
            >
                Back to Cart
            </button>

        </div>
    );
}


export default Checkout;