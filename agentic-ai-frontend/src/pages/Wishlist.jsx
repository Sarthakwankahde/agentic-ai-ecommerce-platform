// src/pages/Wishlist.jsx

import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
    getWishlist,
    removeFromWishlist,
    clearWishlist
} from "../services/wishlistService";

import { addToCart } from "../services/cartService";

function Wishlist() {

    const navigate = useNavigate();

    const [wishlist, setWishlist] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const email = localStorage.getItem("email");


    // ========================================
    // LOAD WISHLIST
    // ========================================

    const loadWishlist = async () => {

        if (!email) {

            setError("User email not found.");
            setLoading(false);

            return;
        }

        try {

            setLoading(true);
            setError("");

            const data = await getWishlist(email);

            setWishlist(data);

        } catch (error) {

            console.error(
                "Failed to load wishlist:",
                error
            );

            setError("Failed to load wishlist.");

        } finally {

            setLoading(false);
        }
    };


    // ========================================
    // INITIAL LOAD
    // ========================================

    useEffect(() => {

        loadWishlist();

    }, []);


    // ========================================
    // REMOVE WISHLIST ITEM
    // ========================================

    const handleRemove = async (
        wishlistItemId
    ) => {

        try {

            await removeFromWishlist(
                email,
                wishlistItemId
            );

            await loadWishlist();

        } catch (error) {

            console.error(
                "Failed to remove wishlist item:",
                error
            );

            setError(
                "Failed to remove wishlist item."
            );
        }
    };


    // ========================================
    // CLEAR WISHLIST
    // ========================================

    const handleClear = async () => {

        try {

            await clearWishlist(email);

            setWishlist([]);

        } catch (error) {

            console.error(
                "Failed to clear wishlist:",
                error
            );

            setError(
                "Failed to clear wishlist."
            );
        }
    };


    // ========================================
    // ADD TO CART
    // ========================================

    const handleAddToCart = async (
        productId
    ) => {

        try {

            await addToCart(
                email,
                productId,
                1
            );

            alert(
                "Product added to cart."
            );

        } catch (error) {

            console.error(
                "Failed to add product to cart:",
                error
            );

            alert(
                "Failed to add product to cart."
            );
        }
    };


    // ========================================
    // LOADING
    // ========================================

    if (loading) {

        return (
            <div>

                <h1>Wishlist</h1>

                <p>
                    Loading wishlist...
                </p>

            </div>
        );
    }


    // ========================================
    // UI
    // ========================================

    return (
        <div className="wishlist-page">

            <h1>My Wishlist</h1>

            {error && (
                <p>
                    {error}
                </p>
            )}


            {wishlist.length === 0 ? (

                <div>

                    <p>
                        Your wishlist is empty.
                    </p>

                    <button
                        onClick={() =>
                            navigate("/products")
                        }
                    >
                        Browse Products
                    </button>

                </div>

            ) : (

                <>

                    <div className="wishlist-items">

                        {wishlist.map((item) => (

                            <div
                                className="wishlist-item"
                                key={item.wishlistId}
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
                                        ₹{item.price}
                                    </p>

                                    <p>
                                        Category:{" "}
                                        {item.categoryName}
                                    </p>

                                </div>


                                <div>

                                    <button
                                        onClick={() =>
                                            navigate(
                                                `/products/${item.productId}`
                                            )
                                        }
                                    >
                                        View Product
                                    </button>


                                    <button
                                        onClick={() =>
                                            handleAddToCart(
                                                item.productId
                                            )
                                        }
                                    >
                                        Add to Cart
                                    </button>


                                    <button
                                        onClick={() =>
                                            handleRemove(
                                                item.wishlistId
                                            )
                                        }
                                    >
                                        Remove
                                    </button>

                                </div>

                            </div>

                        ))}

                    </div>


                    <div className="wishlist-actions">

                        <button
                            onClick={handleClear}
                        >
                            Clear Wishlist
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

export default Wishlist;