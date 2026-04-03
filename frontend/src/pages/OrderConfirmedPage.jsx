import { useNavigate } from "react-router-dom";
import "./OrderConfirmedPage.css";

export default function OrderConfirmedPage() {
  const navigate = useNavigate();

  return (
    <div className="confirmed-page">
      <div className="confirmed-card">
        <div className="confirmed-icon">✓</div>
        <h1 className="confirmed-title">Order Placed!</h1>
        <p className="confirmed-sub">Your order has been placed successfully.</p>
        <button className="back-home-btn" onClick={() => navigate("/")}>
          Back to Menu
        </button>
      </div>
    </div>
  );
}
