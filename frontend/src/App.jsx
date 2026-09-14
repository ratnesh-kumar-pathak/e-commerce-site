import { BrowserRouter, Routes, Route } from "react-router-dom";

import LoginPage from "./pages/LoginPage";
import RegistrationPage from "./pages/RegistrationPage";
import CustomerHome from "./pages/CustomerHome";
import CartPage from "./pages/CartPage";
import AdminLogin from "./pages/AdminLogin";
import AdminDashboard from "./pages/AdminDashboard";

function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route
          path="/"
          element={<LoginPage />}
        />

        <Route
          path="/register"
          element={<RegistrationPage />}
        />

        <Route
          path="/customerhome"
          element={<CustomerHome />}
        />

        <Route
          path="/cart"
          element={<CartPage />}
        />

        <Route
          path="/admin"
          element={<AdminLogin />}
        />

        <Route
          path="/admindashboard"
          element={<AdminDashboard />}
        />

        <Route
          path="/admin-dashboard"
          element={<AdminDashboard />}
        />

      </Routes>
    </BrowserRouter>
  );
}

export default App;