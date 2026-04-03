import { BrowserRouter, Routes, Route } from "react-router-dom";
import { CartProvider } from "./context/CartContext";
import MenuPage from "./pages/MenuPage";
import CartPage from "./pages/CartPage";
import OrderConfirmedPage from "./pages/OrderConfirmedPage";
import "./App.css";

export default function App() {
  return (
    <div className="app-shell">
      <BrowserRouter>
        <CartProvider>
          <Routes>
            <Route path="/"                element={<MenuPage />} />
            <Route path="/cart"            element={<CartPage />} />
            <Route path="/order-confirmed" element={<OrderConfirmedPage />} />
          </Routes>
        </CartProvider>
      </BrowserRouter>
    </div>
  );
}
