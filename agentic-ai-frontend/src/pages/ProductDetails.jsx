import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { getProductById } from "../services/productService";
import { addToCart } from "../services/cartService";
import { addToWishlist } from "../services/wishlistService";
import { useAuth } from "../context/AuthContext";

function ProductDetails() {

    const { id } = useParams();
    const { isAuthenticated } = useAuth();
    const navigate = useNavigate();

    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [cartLoading, setCartLoading] = useState(false);
    const [wishlistLoading, setWishlistLoading] = useState(false);

    const email = localStorage.getItem("email");

    // ========================================
    // LOAD PRODUCT
    // ========================================

    useEffect(() => {

        const loadProduct = async () => {

            try {

                setLoading(true);
                setError("");

                const data = await getProductById(id);

                setProduct(data);

            } catch (error) {

                console.error(
                    "Failed to load product:",
                    error
                );

                setError(
                    "Failed to load product."
                );

            } finally {

                setLoading(false);
            }
        };

        loadProduct();

    }, [id]);


    // ========================================
    // ADD TO CART
    // ========================================

    const handleAddToCart = async () => {

        if (!isAuthenticated) {

            navigate("/login");

            return;
        }

        if (!email) {

            alert("User email not found.");

            return;
        }

        try {

            setCartLoading(true);

            await addToCart(
                email,
                product.id,
                1
            );

            alert("Product added to cart.");

        } catch (error) {

            console.error(
                "Failed to add product to cart:",
                error
            );

            alert(
                error.response?.data?.message ||
                "Failed to add product to cart."
            );

        } finally {

            setCartLoading(false);
        }
    };


    // ========================================
    // ADD TO WISHLIST
    // ========================================

    const handleAddToWishlist = async () => {

        if (!isAuthenticated) {

            navigate("/login");

            return;
        }

        if (!email) {

            alert("User email not found.");

            return;
        }

        try {

            setWishlistLoading(true);

            await addToWishlist(
                email,
                product.id
            );

            alert("Product added to wishlist.");

        } catch (error) {

            console.error(
                "Failed to add product to wishlist:",
                error
            );

            alert(
                error.response?.data?.message ||
                "Failed to add product to wishlist."
            );

        } finally {

            setWishlistLoading(false);
        }
    };


    // ========================================
    // LOADING
    // ========================================

    if (loading) {

        return (
            <div>
                <h1>Product Details</h1>
                <p>Loading product...</p>
            </div>
        );
    }


    // ========================================
    // ERROR
    // ========================================

    if (error || !product) {

        return (
            <div>

                <h1>Product Details</h1>

                <p>
                    {error || "Product not found."}
                </p>

                <button
                    onClick={() => navigate("/products")}
                >
                    Back to Products
                </button>

            </div>
        );
    }


    // ========================================
    // PRODUCT DETAILS
    // ========================================

    return (
        <div className="product-details">

            <button
                onClick={() => navigate("/products")}
            >
                ← Back to Products
            </button>


            <div className="product-details-card">


                {/* ========================================
                    PRODUCT IMAGE
                ======================================== */}

                <div className="product-details-image">

                    <img
                        src={product.imageUrl}
                        alt={product.name}
                    />

                </div>


                {/* ========================================
                    PRODUCT INFORMATION
                ======================================== */}

                <div className="product-details-info">

                    <h1>
                        {product.name}
                    </h1>

                    <p>
                        {product.description}
                    </p>

                    <h2>
                        ₹{product.price}
                    </h2>

                    <p>
                        Category:{" "}
                        {product.categoryName}
                    </p>

                    <p>
                        Available Quantity:{" "}
                        {product.quantity}
                    </p>


                    {/* ========================================
                        ADD TO CART
                    ======================================== */}

                    <button
                        onClick={handleAddToCart}
                        disabled={
                            cartLoading ||
                            product.quantity <= 0
                        }
                    >
                        {cartLoading
                            ? "Adding..."
                            : product.quantity <= 0
                                ? "Out of Stock"
                                : "Add to Cart"}
                    </button>


                    {/* ========================================
                        ADD TO WISHLIST
                    ======================================== */}

                    <button
                        onClick={handleAddToWishlist}
                        disabled={wishlistLoading}
                    >
                        {wishlistLoading
                            ? "Adding..."
                            : "♡ Add to Wishlist"}
                    </button>


                    {/* ========================================
                        GO TO WISHLIST
                    ======================================== */}

                    <button
                        onClick={() =>
                            navigate("/wishlist")
                        }
                    >
                        View Wishlist
                    </button>

                </div>

            </div>

        </div>
    );
}

export default ProductDetails;