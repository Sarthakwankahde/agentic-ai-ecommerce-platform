import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { getProductById } from "../services/productService";
import { addToCart } from "../services/cartService";
import { useAuth } from "../context/AuthContext";

function ProductDetails() {

    const { id } = useParams();
    const { isAuthenticated } = useAuth();
    const navigate = useNavigate();

    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

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

                {/* PRODUCT IMAGE */}

                <div className="product-details-image">

                    <img
                        src={product.imageUrl}
                        alt={product.name}
                    />

                </div>


                {/* PRODUCT INFORMATION */}

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


                    {/* ADD TO CART */}

                    <button>
                        Add to Cart
                    </button>

                </div>

            </div>

        </div>
    );
}

export default ProductDetails;