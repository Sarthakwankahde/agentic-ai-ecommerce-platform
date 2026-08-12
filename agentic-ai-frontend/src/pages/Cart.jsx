import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
    getCart,
    updateCartItem,
    removeCartItem,
    clearCart
} from "../services/cartService";

function Cart() {

    const navigate = useNavigate();

    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const email = localStorage.getItem("email");

    // ========================================
    // LOAD CART
    // ========================================

    const loadCart = async () => {

        if (!email) {
            setError("User email not found.");
            setLoading(false);
            return;
        }

        try {

            setLoading(true);
            setError("");

            const data = await getCart(email);

            setCartItems(data);

        } catch (error) {

            console.error(
                "Failed to load cart:",
                error
            );

            setError("Failed to load cart.");

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
                "Failed to update quantity."
            );
        }
    };

    // ========================================
    // REMOVE ITEM
    // ========================================

    const handleRemove = async (cartItemId) => {

        try {

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
                "Failed to remove item."
            );
        }
    };

    // ========================================
    // CLEAR CART
    // ========================================

    const handleClearCart = async () => {

        try {

            await clearCart(email);

            setCartItems([]);

        } catch (error) {

            console.error(
                "Failed to clear cart:",
                error
            );

            setError(
                "Failed to clear cart."
            );
        }
    };

    // ========================================
    // CALCULATE TOTAL
    // ========================================

    const cartTotal = cartItems.reduce(
        (total, item) =>
            total + item.totalPrice,
        0
    );

    // ========================================
    // LOADING
    // ========================================

    if (loading) {

        return (
            <div>
                <h1>Cart</h1>
                <p>Loading cart...</p>
            </div>
        );
    }

    // ========================================
    // UI
    // ========================================

    return (
        <div className="cart-page">

            <h1>Shopping Cart</h1>

            {error && (
                <p>{error}</p>
            )}

            {cartItems.length === 0 ? (

                <div>

                    <p>
                        Your cart is empty.
                    </p>

                    <button
                        onClick={() =>
                            navigate("/products")
                        }
                    >
                        Continue Shopping
                    </button>

                </div>

            ) : (

                <>

                    {/* CART ITEMS */}

                    <div className="cart-items">

                        {cartItems.map((item) => (

                            <div
                                className="cart-item"
                                key={item.cartId}
                            >

                                <img
                                    src={item.imageUrl}
                                    alt={item.productName}
                                />

                                <div>

                                    <h2>
                                        {item.productName}
                                    </h2>

                                    <p>
                                        Price: ₹{item.price}
                                    </p>

                                    <p>
                                        Total: ₹
                                        {item.totalPrice}
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
                                        {item.quantity}{" "}
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

                        ))}

                    </div>

                    {/* CART SUMMARY */}

                    <div className="cart-summary">

                        <h2>
                            Cart Total: ₹{cartTotal}
                        </h2>

                        <button
                            onClick={handleClearCart}
                        >
                            Clear Cart
                        </button>

                        <button
                            onClick={() =>
                                navigate("/products")
                            }
                        >
                            Continue Shopping
                        </button>

                    </div>

                </>

            )}

        </div>
    );
}

export default Cart;