# 🛒 Full-Stack E-Commerce Web Application

A **full-stack e-commerce platform** built with Java Spring Boot and React.js.

---

## 🌟 Key Application Features
*   **🔐 Identity & Access Control:** JWT authentication and Role-Based Access Control (RBAC).
*   **📦 Inventory Control:** Add, modify, view, and delete product listings.
*   **🛒 Interactive Basket:** Persistent shopping cart with real-time totals.
*   **💳 Merchant Gateway:** Secure checkout powered by the Razorpay API.
*   **📋 Order Management:** Purchase tracking and sales invoices.
*   **📱 Universal Presentation:** Mobile-first responsive layout.
*   **🔌 Decoupled Communication:** RESTful APIs.

---

## 💻 System Tech Stack
*   **Backend:** Java Spring Boot, Hibernate ORM, RESTful APIs, JWT.
*   **Frontend:** React.js, Redux, HTML5, CSS3, JavaScript ES6+.
*   **Infrastructure:** MySQL, Git, Apache Maven, Postman, IntelliJ IDEA.

---

## 🛣️ API Endpoints Reference
The application utilizes standard RESTful endpoints for authentication, product catalog management, shopping cart operations, order processing, and payment integration via Razorpay.

---

## 🚀 Quick Setup Guide
1. **Database:** Create a MySQL schema named `ecommerce` and update `application.properties`.
2. **Backend:** Run `mvn clean install` and `mvn spring-boot:run` in the backend directory (runs on port 8080).
3. **Frontend:** Run `npm install` and `npm start` in the frontend directory (runs on port 3000).
