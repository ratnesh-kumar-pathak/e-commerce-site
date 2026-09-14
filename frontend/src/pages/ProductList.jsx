import React from "react";
import "../assets/style.css";

export function ProductList({ products, onAddToCart }) {

  if (!products || products.length === 0) {
    return (
      <p className="no-products">
        No products available.
      </p>
    );
  }

  return (
    <div className="product-list">

      <div className="product-grid">

        {products.map((product) => (

          <div
            key={product.productId || product.product_id}
            className="product-card"
          >

            <img
              src={
                product.images &&
                product.images.length > 0
                  ? product.images[0]
                  : "https://via.placeholder.com/250"
              }
              alt={product.name}
              className="product-image"
              onError={(e) => {
                e.target.src =
                  "https://via.placeholder.com/250";
              }}
            />

            <div className="product-info">

              <h3 className="product-name">
                {product.name}
              </h3>

              <p className="product-description">
                {product.description}
              </p>

              <p className="product-price">
                ₹ {product.price}
              </p>

              <button
                className="add-to-cart-btn"
                onClick={() =>
                  onAddToCart &&
                  onAddToCart(
                    product.productId ||
                    product.product_id
                  )
                }
              >
                Add To Cart
              </button>

            </div>

          </div>

        ))}

      </div>

    </div>
  );
}