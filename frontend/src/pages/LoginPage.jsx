import { useState } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/Logo.png";

import "../css/login.css";
import "../assets/style.css";

export default function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  const handleSignIn = async (e) => {
    e.preventDefault();

    setError(null);

    if (!username.trim() || !password.trim()) {
      setError("Username and password are required");
      return;
    }

    try {
      const response = await fetch((import.meta.env.VITE_API_URL || "") + "/api/auth/login",
  {
    method: "POST",
    credentials: "include",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      username,
      password
    })
  }
);

      const data = await response.json();

      if (response.ok) {

  localStorage.setItem("token", data.token);

  // store the username entered in login form
  localStorage.setItem("username", username);

  alert("Login Successful");

  navigate("/customerhome");
} else {
        throw new Error(
          data.error || "Invalid username or password"
        );
      }
    } catch (err) {
      setError(err.message || "Unexpected error occurred");
    }
  };

  return (
    <div className="container">
      <div className="login-box">

          {/* Logo */}
          <div className="logo">
            <img
              src={logo}
              alt="ShopKart Logo"
              className="logo-image"
            />
          </div>

          {/* Title */}
          <h1 className="form-title">ShopKart</h1>

          <p className="login-subtitle">
            Electronics Store
          </p>

          {/* Error Message */}
          {error && (
            <p className="error-message">
              {error}
            </p>
          )}

          {/* Login Form */}
          <form
            onSubmit={handleSignIn}
            className="form-content"
          >

            <div className="form-group">
              <label
                htmlFor="username"
                className="form-label"
              >
                Username
              </label>

              <input
                id="username"
                type="text"
                placeholder="Enter your username"
                value={username}
                onChange={(e) =>
                  setUsername(e.target.value)
                }
                required
                className="form-input"
              />
            </div>

            <div className="form-group">
              <label
                htmlFor="password"
                className="form-label"
              >
                Password
              </label>

              <input
                id="password"
                type="password"
                placeholder="Enter your password"
                value={password}
                onChange={(e) =>
                  setPassword(e.target.value)
                }
                required
                className="form-input"
              />
            </div>

            <button
              type="submit"
              className="form-button"
            >
              Sign In
            </button>

          </form>

          <div className="form-footer">
            <a
              href="/register"
              className="form-link"
            >
              New User? Sign up here
            </a>
          </div>

      </div>
    </div>
  );
}
