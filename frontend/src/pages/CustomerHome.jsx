import { useState, useEffect } from "react";
import "../assets/style.css";
import "../css/CustomerHome.css";
import { ProductList } from "./ProductList";
import { FaShoppingCart } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

export default function CustomerHome() {

  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [username, setUsername] = useState("Guest");
  const [cartCount, setCartCount] = useState(0);

  const navigate = useNavigate();

  useEffect(() => {

    const loggedUser =
      localStorage.getItem("username");

    if (loggedUser) {
      setUsername(loggedUser);
    }

    fetchProducts("Mobiles");

  }, []);

  useEffect(() => {

    if (
      username &&
      username !== "Guest"
    ) {
      fetchCartCount();
    }

  }, [username]);

  // =========================
  // FETCH PRODUCTS
  // =========================

  const fetchProducts = async (category) => {

    try {

      setLoading(true);

      const token =
        localStorage.getItem("token");

      const response = await fetch((import.meta.env.VITE_API_URL || "") + `/api/products?category=${category}`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      if (!response.ok) {
        throw new Error(
          "Failed to fetch products"
        );
      }

      const data =
        await response.json();

      console.log(
        "API Response:",
        data
      );

      setProducts(
        data.products || []
      );

    } catch (error) {

      console.error(
        "Error fetching products:",
        error
      );

      setProducts([]);

    } finally {

      setLoading(false);

    }
  };

  // =========================
  // FETCH CART COUNT
  // =========================

  const fetchCartCount = async () => {

    try {

      const token =
        localStorage.getItem("token");

      const response = await fetch((import.meta.env.VITE_API_URL || "") + `/api/cart/items/count?username=${username}`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      if (response.ok) {

        const count =
          await response.json();

        setCartCount(count);
      }

    } catch (error) {

      console.error(
        "Cart Count Error:",
        error
      );
    }
  };

  // =========================
  // ADD TO CART
  // =========================

  const handleAddToCart = async (
    productId
  ) => {

    console.log(
      "Username:",
      username
    );

    console.log(
      "ProductId:",
      productId
    );

    try {

      const token =
        localStorage.getItem("token");

      const response = await fetch((import.meta.env.VITE_API_URL || "") + "/api/cart/add",
        {
          method: "POST",

          headers: {
            "Content-Type":
              "application/json",
            Authorization:
              `Bearer ${token}`
          },

          body: JSON.stringify({
            username: username,
            productId: productId,
            quantity: 1
          })
        }
      );

      const text =
        await response.text();

      console.log(
        "Status:",
        response.status
      );

      console.log(
        "Response:",
        text
      );

      if (response.ok) {

        alert(
          "Product Added To Cart"
        );

        fetchCartCount();
      }

    } catch (error) {

      console.error(
        "Cart Error:",
        error
      );
    }
  };

  return (

    <div className="customer-homepage">

      {/* NAVBAR */}

      <div className="navbar">

        <div className="logo-section">
          🛒 <span>ShopKart</span>
        </div>

        <div className="nav-right">

          <div
            className="cart-icon"
            onClick={() =>
              navigate("/cart")
            }
          >
            <FaShoppingCart />

            <span className="cart-count">
              {cartCount}
            </span>
          </div>

          <div className="user-info">
            👤 {username}
          </div>

          <button 
            onClick={() => {
              localStorage.removeItem("token");
              localStorage.removeItem("username");
              navigate("/");
            }}
            style={{ 
              marginLeft: "15px", 
              padding: "6px 12px", 
              background: "#ef4444", 
              color: "white", 
              border: "none", 
              borderRadius: "6px", 
              cursor: "pointer",
              fontWeight: "bold",
              fontSize: "14px"
            }}
          >
            Logout
          </button>

        </div>

      </div>

      {/* CATEGORY NAVIGATION */}

      <div className="navigation">

        <button
          onClick={() =>
            fetchProducts("Mobiles")
          }
        >
          Mobiles
        </button>

        <button
          onClick={() =>
            fetchProducts("Laptops")
          }
        >
          Laptops
        </button>

        <button
          onClick={() =>
            fetchProducts(
              "Headphones"
            )
          }
        >
          Headphones
        </button>

        <button
          onClick={() =>
            fetchProducts(
              "Smart Watches"
            )
          }
        >
          Smart Watches
        </button>

      </div>

      {/* PRODUCTS */}

      <div className="main-content">

        {loading ? (

          <h2
            style={{
              textAlign: "center",
              marginTop: "50px",
              color: "#2563eb"
            }}
          >
            Loading Products...
          </h2>

        ) : (

          <ProductList
            products={products}
            onAddToCart={
              handleAddToCart
            }
          />

        )}

      </div>

      {/* FOOTER */}

      <div className="footer">
        © 2026 ShopKart Electronics Store
      </div>

    </div>
  );
}