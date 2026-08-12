import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { addToCart } from "../services/cartService";

import {
    getAllProducts,
    getAllCategories,
    searchProducts,
    getProductsByCategory
} from "../services/productService";

function Products() {
    const navigate = useNavigate();
    const { isAuthenticated } = useAuth();
    const email = localStorage.getItem("email");

    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [keyword, setKeyword] = useState("");
    const [categoryId, setCategoryId] = useState("");

    // ========================================
    // LOAD PRODUCTS
    // ========================================

    const loadProducts = async () => {

        try {

            setLoading(true);
            setError("");

            const data = await getAllProducts();

            setProducts(data.content || []);

        } catch (error) {

            console.error("Failed to load products:", error);

            setError("Failed to load products.");

        } finally {

            setLoading(false);
        }
    };


    // ========================================
    // LOAD CATEGORIES
    // ========================================

    const loadCategories = async () => {

        try {

            const data = await getAllCategories();

            setCategories(data);

        } catch (error) {

            console.error(
                "Failed to load categories:",
                error
            );
        }
    };


    // ========================================
    // INITIAL LOAD
    // ========================================

    useEffect(() => {

        loadProducts();
        loadCategories();

    }, []);


    // ========================================
    // SEARCH
    // ========================================

    const handleSearch = async () => {

        if (!keyword.trim()) {

            loadProducts();

            return;
        }

        try {

            setLoading(true);

            const data = await searchProducts(keyword);

            setProducts(data);

        } catch (error) {

            console.error(
                "Search failed:",
                error
            );

            setError("Search failed.");

        } finally {

            setLoading(false);
        }
    };


    // ========================================
    // CATEGORY FILTER
    // ========================================

    const handleCategoryChange = async (event) => {

        const value = event.target.value;

        setCategoryId(value);

        if (!value) {

            loadProducts();

            return;
        }

        try {

            setLoading(true);

            const data =
                await getProductsByCategory(value);

            setProducts(data);

        } catch (error) {

            console.error(
                "Category filter failed:",
                error
            );

            setError(
                "Failed to filter products."
            );

        } finally {

            setLoading(false);
        }
    };
      // ========================================
     // ADD TO CART
     // ========================================

    const handleAddToCart = async (productId) => {

        if (!isAuthenticated) {

            navigate("/login");

            return;
        }

        if (!email) {

            alert("User email not found.");

            return;
        }

        try {

            await addToCart(
                email,
                productId,
                1
            );

            alert("Product added to cart.");

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
                <h1>Products</h1>
                <p>Loading products...</p>
            </div>
        );
    }


    // ========================================
    // UI
    // ========================================

    return (
        <div className="products-page">

            <h1>Products</h1>

            {/* SEARCH */}

            <div>

                <input
                    type="text"
                    placeholder="Search products..."
                    value={keyword}
                    onChange={(e) =>
                        setKeyword(e.target.value)
                    }
                />

                <button onClick={handleSearch}>
                    Search
                </button>

            </div>


            {/* CATEGORY */}

            <div>

                <select
                    value={categoryId}
                    onChange={handleCategoryChange}
                >

                    <option value="">
                        All Categories
                    </option>

                    {categories.map((category) => (

                        <option
                            key={category.id}
                            value={category.id}
                        >
                            {category.name}
                        </option>

                    ))}

                </select>

            </div>


            {/* ERROR */}

            {error && (
                <p>{error}</p>
            )}


            {/* PRODUCTS */}

            <div className="product-grid">

                {products.length === 0 ? (

                    <p>
                        No products found.
                    </p>

                ) : (

                    products.map((product) => (

                        <div
                            className="product-card"
                            key={product.id}
                        >

                            <img
                                src={product.imageUrl}
                                alt={product.name}
                            />

                            <h2>
                                {product.name}
                            </h2>

                            <p>
                                {product.description}
                            </p>

                            <p>
                                ₹{product.price}
                            </p>

                            <p>
                                Category:{" "}
                                {product.categoryName}
                            </p>

                            <p>
                                Quantity:{" "}
                                {product.quantity}
                            </p>

                            <button>
                                Add to Cart
                            </button>
                            <button
                                onClick={() =>
                                    navigate(`/products/${product.id}`)
                                }
                            >
                                View Details
                            </button>

                        </div>

                    ))

                )}

            </div>

        </div>
    );
}

export default Products;