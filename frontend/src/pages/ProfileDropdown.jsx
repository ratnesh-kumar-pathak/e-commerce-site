import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import useravatar from "../assets/useravatar.png";
import "../assets/style.css";

export function ProfileDropdown({ username }) {

  const [isOpen, setIsOpen] = useState(false);

  const navigate = useNavigate();

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const handleLogout = async () => {

    try {

      const token = localStorage.getItem("token");

      const response = await fetch((import.meta.env.VITE_API_URL || "") + "/api/auth/logout",
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      if (response.ok) {

        localStorage.removeItem("token");
        localStorage.removeItem("username");

        alert("Logout Successful");

        navigate("/");

      } else {

        alert("Logout Failed");
      }

    } catch (error) {

      console.error("Logout Error:", error);

      localStorage.removeItem("token");
      localStorage.removeItem("username");

      navigate("/");
    }
  };

  const handleOrdersClick = () => {
    navigate("/orders");
  };

  return (

    <div className="profile-dropdown">

      <button
        className="profile-button"
        onClick={toggleDropdown}
      >

        <img
          src={useravatar}
          alt="User Avatar"
          className="user-avatar"
        />

        <span className="username">
          {username || "Guest"}
        </span>

      </button>

      {isOpen && (

        <div className="dropdown-menu">

          <button
            className="dropdown-item"
            onClick={handleOrdersClick}
          >
            Orders
          </button>

          <button
            className="dropdown-item"
            onClick={handleLogout}
          >
            Logout
          </button>

        </div>

      )}

    </div>
  );
}