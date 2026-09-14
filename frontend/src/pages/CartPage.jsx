import  { useEffect, useState } from "react";
import "../css/CartPage.css";
import { Footer } from "./Footer";
import { useNavigate } from "react-router-dom";
import { FaPlus, FaMinus, FaTrash } from "react-icons/fa";

const CartPage = () => {
  const [cartItems, setCartItems] = useState([]);
  const [overallPrice, setOverallPrice] = useState(0);
  const [username, setUsername] = useState("");
  const [subtotal, setSubtotal] = useState(0);
  const navigate = useNavigate(); // To redirect users after successful payment

  // Fetch cart items on component load
  useEffect(() => {
    const fetchCartItems = async () => {
      try {
       const loggedUser =
  localStorage.getItem("username");

const token = localStorage.getItem("token");

console.log("TOKEN =", token);

const response = await fetch((import.meta.env.VITE_API_URL || "") + `/api/cart/items?username=${loggedUser}`,
  {
    method: "GET",
    headers: {
      "Authorization": `Bearer ${token}`
    }
  }
);
        if (!response.ok) throw new Error("Failed to fetch cart items");
        const data = await response.json();

        setCartItems(
          data?.cart?.products.map((item) => ({
            ...item,
            total_price: parseFloat(item.total_price).toFixed(2),
            price_per_unit: parseFloat(item.price_per_unit).toFixed(2),
          })) || []
        );
        setOverallPrice(parseFloat(data?.cart?.overall_total_price || 0).toFixed(2));
        setUsername(data?.username || ""); // Save the username from the response
      } catch (error) {
        console.error("Error fetching cart items:", error);
      }
    };

    fetchCartItems();
  }, []);

  // Calculate subtotal whenever cart items change
  useEffect(() => {
    const total = cartItems
      .reduce((total, item) => total + parseFloat(item.total_price), 0)
      .toFixed(2);
    setSubtotal(total);
  }, [cartItems]);

  // Remove item from the cart
  const handleRemoveItem = async (productId) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch((import.meta.env.VITE_API_URL || "") + "/api/cart/delete", {
        method: "DELETE",
        headers: { 
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        credentials: "include",
        body: JSON.stringify({ username, productId }),
      });
      if (response.status === 204) {
        setCartItems((prevItems) => prevItems.filter((item) => item.product_id !== productId));
      } else throw new Error("Failed to remove item");
    } catch (error) {
      console.error("Error removing item:", error);
    }
  };

  // Update quantity of an item
  const handleQuantityChange = async (productId, newQuantity) => {
    try {
      if (newQuantity <= 0) {
        handleRemoveItem(productId);
        return;
      }
      const token = localStorage.getItem("token");
      const response = await fetch((import.meta.env.VITE_API_URL || "") + "/api/cart/update", {
        method: "PUT",
        headers: { 
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        credentials: "include",
        body: JSON.stringify({ username, productId, quantity: newQuantity }),
      });
      if (response.ok) {
        setCartItems((prevItems) =>
          prevItems.map((item) =>
            item.product_id === productId
              ? {
                  ...item,
                  quantity: newQuantity,
                  total_price: (item.price_per_unit * newQuantity).toFixed(2),
                }
              : item
          )
        );
      } else throw new Error("Failed to update quantity");
    } catch (error) {
      console.error("Error updating quantity:", error);
    }
  };

  // Razorpay integration for payment
 const handleCheckout = async () => {
  try {

    const requestBody = {
      totalAmount: subtotal,
      cartItems: cartItems.map((item) => ({
        productId: item.product_id,
        quantity: item.quantity,
        price: item.price_per_unit,
      })),
    };

    // ADD THESE LOGS HERE
    console.log("Subtotal =", subtotal);
    console.log("Cart Items =", cartItems);
    console.log("Request Body =", requestBody);

    const token = localStorage.getItem("token");

const response = await fetch((import.meta.env.VITE_API_URL || "") + "/api/payment/create",
  {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`
    },
    body: JSON.stringify(requestBody),
  }
);

    console.log("Payment Create Status =", response.status);

    if (!response.ok) {
      const errorText = await response.text();
      console.log("Backend Error =", errorText);
      throw new Error(errorText);
    }

    const razorpayOrderId = await response.text();

    console.log("Razorpay Order ID =", razorpayOrderId);

      // Open Razorpay checkout interface
      const options = {
        key: "rzp_test_So4zRTYiAGVK6s", // Replace with your Razorpay Key ID
        amount: subtotal * 100, // Razorpay expects amount in paise
        currency: "INR",
        name: "SalesSavvy",
        description: "Test Transaction",
        order_id: razorpayOrderId,
        handler: async function (response) {
          try {
            // Payment success, verify on backend
            const token = localStorage.getItem("token");

const verifyResponse = await fetch((import.meta.env.VITE_API_URL || "") + "/api/payment/verify",
  {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`
    },
    body: JSON.stringify({
      razorpayOrderId: response.razorpay_order_id,
      razorpayPaymentId: response.razorpay_payment_id,
      razorpaySignature: response.razorpay_signature,
    }),
  }
);
            const result = await verifyResponse.text();
            if (verifyResponse.ok) {
              alert("Payment verified successfully!");
              navigate("/customerhome"); // Redirect to Customer Home Page
            } else {
              alert("Payment verification failed: " + result);
            }
          } catch (error) {
            console.error("Error verifying payment:", error);
            alert("Payment verification failed. Please try again.");
          }
        },
        prefill: {
          name: username,
          email: "test@example.com",
          contact: "9999999999",
        },
        theme: {
          color: "#3399cc",
        },
      };

      const rzp = new window.Razorpay(options);
      rzp.open();
    } catch (error) {
      alert("Payment failed. Please try again.");
      console.error("Error during checkout:", error);
    }
  };

  const totalProducts = () => cartItems.reduce((acc, item) => acc + item.quantity, 0);
const shipping = 370; // Hardcoded shipping value

  if (cartItems.length === 0) {
  return (
    <div className="empty-cart">
      <h2>Your Cart is Empty</h2>
      <p>Add some products to continue shopping.</p>

      <button
        onClick={() => navigate("/customerhome")}
      >
        Continue Shopping
      </button>
    </div>
  );
}
const handleLogout = async () => {
   await fetch((import.meta.env.VITE_API_URL || "") + "/api/auth/logout", {
      method: "POST",
      credentials: "include"
   });
};
return (
  <div style={{ width: "100vw" }}>

    

    <div className="cart-container">

      <div className="cart-page">

        <div className="cart-header">
          <button 
             onClick={() => navigate("/customerhome")}
             style={{ background: "transparent", border: "none", color: "#2563eb", cursor: "pointer", fontSize: "16px", marginBottom: "10px", padding: 0, fontWeight: "bold" }}
          >
            &larr; Back to Home
          </button>
          <h2>Shopping Cart</h2>
          <p>
            You have {cartItems.length}
            {" "}items in your cart
          </p>
        </div>

        <div className="cart-items">

          {cartItems.map((item) => (

            <div
              key={item.product_id}
              className="cart-item"
            >

              <img
                src={
                  item.image_url ||
                  "https://via.placeholder.com/80?text=No+Image"
                }
                alt={item.name}
              />

              <div className="item-details">

                <div className="item-info">
                  <h3>{item.name}</h3>
                  <p>{item.description}</p>
                </div>

                <div className="item-actions">

                  <div className="quantity-controls">

                    <button
                      onClick={() =>
                        handleQuantityChange(
                          item.product_id,
                          item.quantity - 1
                        )
                      }
                    >
                      <FaMinus size={14} />
                    </button>

                    <span className="quantity-display">
                      {item.quantity}
                    </span>

                    <button
                      onClick={() =>
                        handleQuantityChange(
                          item.product_id,
                          item.quantity + 1
                        )
                      }
                    >
                      <FaPlus size={14} />
                    </button>

                  </div>

                  <span className="price">
                    ₹{item.total_price}
                  </span>

                  <button
                    className="remove-btn"
                    onClick={() =>
                      handleRemoveItem(
                        item.product_id
                      )
                    }
                  >
                    <FaTrash size={18} />
                  </button>

                </div>

              </div>

            </div>

          ))}

        </div>

      </div>

      <div className="checkout-section">

        <h2>Order Summary</h2>

        <div className="checkout-summary">

          <div className="summary-row">
            <span>Subtotal</span>
            <span>₹{subtotal}</span>
          </div>

          <div className="summary-row">
            <span>Shipping</span>
            <span>₹{shipping}</span>
          </div>

          <div className="summary-row">
            <span>Total Products</span>
            <span>{totalProducts()}</span>
          </div>

          <div className="summary-row total">
            <span>Total</span>
            <span>
              ₹{(
                parseFloat(subtotal) +
                parseFloat(shipping)
              ).toFixed(2)}
            </span>
          </div>

          <button
            className="checkout-button"
            onClick={handleCheckout}
          >
            Proceed to Checkout
          </button>

        </div>

      </div>

    </div>

    <Footer />

  </div>
);
};
export default CartPage;