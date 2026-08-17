
// src/pages/Cart.jsx

import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
    getCart,
    updateCartItem,
    removeCartItem,
    clearCart
} from "../services/cartService";

import { placeOrder } from "../services/orderService";

import {
    createPaymentOrder,
    verifyPayment
} from "../services/paymentService";


function Cart() {

    const navigate = useNavigate();

    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [orderLoading, setOrderLoading] =
        useState(false);

    const email =
        localStorage.getItem("email");


    // ========================================
    // LOAD CART
    // ========================================

    const loadCart = async () => {

        if (!email) {

            setError(
                "User email not found."
            );

            setLoading(false);

            return;
        }

        try {

            setLoading(true);
            setError("");

            const data =
                await getCart(email);

            setCartItems(data);

        } catch (error) {

            console.error(
                "Failed to load cart:",
                error
            );

            setError(
                error.response?.data?.message ||
                "Failed to load cart."
            );

        } finally {

            setLoading(false);
        }
    };


    // ========================================
    // INITIAL LOAD
    // ========================================

    useEffect(() => {

        loadCart();

    }, []);


    // ========================================
    // UPDATE QUANTITY
    // ========================================

    const handleUpdateQuantity = async (
        cartItemId,
        quantity
    ) => {

        if (quantity < 1) {
            return;
        }

        try {

            setError("");

            await updateCartItem(
                email,
                cartItemId,
                quantity
            );

            await loadCart();

        } catch (error) {

            console.error(
                "Failed to update cart item:",
                error
            );

            setError(
                error.response?.data?.message ||
                "Failed to update quantity."
            );
        }
    };


    // ========================================
    // REMOVE ITEM
    // ========================================

    const handleRemove = async (
        cartItemId
    ) => {

        try {

            setError("");

            await removeCartItem(
                email,
                cartItemId
            );

            await loadCart();

        } catch (error) {

            console.error(
                "Failed to remove cart item:",
                error
            );

            setError(
                error.response?.data?.message ||
                "Failed to remove item."
            );
        }
    };


    // ========================================
    // CLEAR CART
    // ========================================

    const handleClearCart = async () => {

        try {

            setError("");

            await clearCart(email);

            setCartItems([]);

        } catch (error) {

            console.error(
                "Failed to clear cart:",
                error
            );

            setError(
                error.response?.data?.message ||
                "Failed to clear cart."
            );
        }
    };


    // ========================================
    // LOAD RAZORPAY SCRIPT
    // ========================================

    const loadRazorpayScript = () => {

        return new Promise((resolve) => {

            if (
                document.querySelector(
                    'script[src="https://checkout.razorpay.com/v1/checkout.js"]'
                )
            ) {

                resolve(true);

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

        if (cartItems.length === 0) {

            setError(
                "Your cart is empty."
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
                "Order Created:",
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
                "Payment Order Created:",
                payment
            );


            // ========================================
            // STEP 4
            // RAZORPAY CHECKOUT
            // ========================================

            const razorpayKey =
                import.meta.env
                    .VITE_RAZORPAY_KEY_ID;


            if (!razorpayKey) {

                throw new Error(
                    "Razorpay Key ID is not configured."
                );
            }


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
                            // STEP 5
                            // VERIFY PAYMENT
                            // ========================================

                            const verifiedPayment =
                                await verifyPayment(

                                    response.razorpay_order_id,

                                    response.razorpay_payment_id,

                                    response.razorpay_signature
                                );


                            console.log(
                                "Payment Verified:",
                                verifiedPayment
                            );


                            alert(
                                "Payment successful!"
                            );


                            // ========================================
                            // STEP 6
                            // GO TO ORDER DETAILS
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
                // PREFILL USER
                // ========================================

                prefill: {

                    email: email || ""
                },


                theme: {

                    color: "#3399cc"
                }
            };


            // ========================================
            // OPEN RAZORPAY
            // ========================================

            const razorpay =
                new window.Razorpay(
                    options
                );


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


            razorpay.open();


        } catch (error) {

            console.error(
                "Failed to place order/payment:",
                error
            );

            setError(
                error.response?.data?.message ||
                error.message ||
                "Failed to place order/payment."
            );

            setOrderLoading(false);
        }
    };


    // ========================================
    // CALCULATE CART TOTAL
    // ========================================

    const cartTotal =
        cartItems.reduce(
            (total, item) =>
                total +
                Number(
                    item.totalPrice || 0
                ),
            0
        );


    // ========================================
    // LOADING
    // ========================================

    if (loading) {

        return (
            <div>

                <h1>
                    Cart
                </h1>

                <p>
                    Loading cart...
                </p>

            </div>
        );
    }


    // ========================================
    // UI
    // ========================================

    return (

        <div className="cart-page">

            <h1>
                Shopping Cart
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
                EMPTY CART
            ======================================== */}

            {cartItems.length === 0 ? (

                <div>

                    <p>
                        Your cart is empty.
                    </p>

                    <button
                        onClick={() =>
                            navigate(
                                "/products"
                            )
                        }
                    >
                        Continue Shopping
                    </button>

                </div>

            ) : (

                <>

                    {/* ========================================
                        CART ITEMS
                    ======================================== */}

                    <div className="cart-items">

                        {cartItems.map(
                            (item) => (

                                <div
                                    className="cart-item"
                                    key={item.cartId}
                                >

                                    {/* PRODUCT IMAGE */}

                                    <img
                                        src={
                                            item.imageUrl
                                        }
                                        alt={
                                            item.productName
                                        }
                                    />


                                    {/* PRODUCT INFORMATION */}

                                    <div>

                                        <h2>
                                            {
                                                item.productName
                                            }
                                        </h2>

                                        <p>
                                            Price: ₹
                                            {
                                                item.price
                                            }
                                        </p>

                                        <p>
                                            Total: ₹
                                            {
                                                item.totalPrice
                                            }
                                        </p>

                                    </div>


                                    {/* QUANTITY */}

                                    <div>

                                        <button
                                            onClick={() =>
                                                handleUpdateQuantity(
                                                    item.cartId,
                                                    item.quantity - 1
                                                )
                                            }
                                            disabled={
                                                item.quantity <= 1
                                            }
                                        >
                                            -
                                        </button>

                                        <span>
                                            {" "}
                                            {
                                                item.quantity
                                            }{" "}
                                        </span>

                                        <button
                                            onClick={() =>
                                                handleUpdateQuantity(
                                                    item.cartId,
                                                    item.quantity + 1
                                                )
                                            }
                                        >
                                            +
                                        </button>

                                    </div>


                                    {/* REMOVE */}

                                    <button
                                        onClick={() =>
                                            handleRemove(
                                                item.cartId
                                            )
                                        }
                                    >
                                        Remove
                                    </button>

                                </div>
                            )
                        )}

                    </div>


                    {/* ========================================
                        CART SUMMARY
                    ======================================== */}

                    <div className="cart-summary">

                        <h2>
                            Cart Total: ₹
                            {cartTotal}
                        </h2>


                        {/* CLEAR CART */}

                        <button
                            onClick={
                                handleClearCart
                            }
                        >
                            Clear Cart
                        </button>


                        {/* CONTINUE SHOPPING */}

                        <button
                            onClick={() =>
                                navigate(
                                    "/products"
                                )
                            }
                        >
                            Continue Shopping
                        </button>


                        {/* ========================================
                            PLACE ORDER + PAYMENT
                        ======================================== */}

                        <button
                            onClick={
                                handlePlaceOrder
                            }
                            disabled={
                                orderLoading
                            }
                        >
                            {orderLoading
                                ? "Processing Payment..."
                                : "Place Order & Pay"}
                        </button>

                    </div>

                </>
            )}

        </div>
    );
}

export default Cart;