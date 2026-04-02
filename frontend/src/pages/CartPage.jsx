import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { validatePromoCode, placeOrder } from "../services/cartService";
import "./CartPage.css";

export default function CartPage() {
  const navigate = useNavigate();
  const {
    items, subtotal, total, totalItems,
    DELIVERY_FEE, discount, promoCode,
    updateQuantity, removeItem, clearCart,
    applyPromo, removePromo,
  } = useCart();

  const [promoInput, setPromoInput] = useState("");
  const [promoError, setPromoError] = useState("");
  const [promoLoading, setPromoLoading] = useState(false);
  const [orderLoading, setOrderLoading] = useState(false);
  const [note, setNote] = useState("");
  const [showNote, setShowNote] = useState(false);

  const handleApplyPromo = async () => {
    if (!promoInput.trim()) return;
    setPromoLoading(true);
    setPromoError("");
    try {
      const result = await validatePromoCode(promoInput, subtotal);
      applyPromo(promoInput.toUpperCase(), result.discount);
      setPromoInput("");
    } catch {
      setPromoError("Invalid promo code. Try FOOD20 or WELCOME50");
    } finally {
      setPromoLoading(false);
    }
  };

  const handleCheckout = async () => {
    setOrderLoading(true);
    const result = await placeOrder(items, promoCode, total);
    setOrderLoading(false);
    if (result.success) {
      clearCart();
      alert(`Order placed! Order ID: ${result.orderId}`);
      navigate("/");
    }
  };

  if (items.length === 0) {
    return (
      <div className="cart-page">
        <div className="cart-header">
          <button className="back-btn" onClick={() => navigate(-1)}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
              <path d="M19 12H5M12 5l-7 7 7 7" />
            </svg>
          </button>
          <div className="header-title">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2">
              <path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4z" /><line x1="3" y1="6" x2="21" y2="6" /><path d="M16 10a4 4 0 01-8 0" />
            </svg>
            Your Cart
          </div>
        </div>
        <div className="empty-cart">
          <div className="empty-icon">🛒</div>
          <h3>Your cart is empty</h3>
          <p>Add items from the menu to get started</p>
          <button className="browse-btn" onClick={() => navigate("/")}>Browse Menu</button>
        </div>
      </div>
    );
  }

  return (
    <div className="cart-page">
      {/* Header */}
      <div className="cart-header">
        <button className="back-btn" onClick={() => navigate(-1)}>
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
            <path d="M19 12H5M12 5l-7 7 7 7" />
          </svg>
        </button>
        <div className="header-title">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2">
            <path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4z" /><line x1="3" y1="6" x2="21" y2="6" /><path d="M16 10a4 4 0 01-8 0" />
          </svg>
          Your Cart
        </div>
        <button className="clear-btn" onClick={clearCart}>clear cart</button>
      </div>

      <div className="cart-body">
        {/* Delivery Info */}
        <div className="delivery-card">
          <div className="delivery-row">
            <div className="delivery-left">
              <span className="delivery-icon">📍</span>
              <div>
                <p className="delivery-label">Delivering to</p>
                <p className="delivery-address">Home - Dhanmondi 27</p>
                <p className="delivery-sub">Road 5, House 12, Dhaka 1209</p>
              </div>
            </div>
            <button className="change-btn">Change</button>
          </div>
          <div className="delivery-time">
            <span>🕐</span> Est. delivery in <strong>25-30 mins</strong>
          </div>
        </div>

        {/* Cart Items */}
        <div className="cart-items">
          {items.map((item, index) => (
            <div key={index} className="cart-item">
              <div className="cart-item-top">
                <img src={item.image} alt={item.name} className="cart-item-img" />
                <div className="cart-item-info">
                  <div className="cart-item-name-row">
                    <span className="cart-item-name">{item.name}</span>
                    <button className="delete-btn" onClick={() => removeItem(index)}>
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#ef4444" strokeWidth="2">
                        <polyline points="3 6 5 6 21 6" /><path d="M19 6l-1 14H6L5 6" /><path d="M10 11v6M14 11v6" /><path d="M9 6V4h6v2" />
                      </svg>
                    </button>
                  </div>
                  <p className="cart-item-restaurant">{item.restaurantName}</p>
                  {item.customizationLabel && (
                    <p className="cart-item-custom">{item.customizationLabel}</p>
                  )}
                  <span className="cart-item-price">৳{item.price}</span>
                </div>
              </div>
              <div className="cart-item-bottom">
                <div className="qty-row">
                  <button className="qty-btn" onClick={() => updateQuantity(index, item.quantity - 1)}>−</button>
                  <span className="qty-val">{item.quantity}</span>
                  <button className="qty-btn" onClick={() => updateQuantity(index, item.quantity + 1)}>+</button>
                </div>
                <span className="item-subtotal">৳{item.price * item.quantity}</span>
              </div>
            </div>
          ))}
        </div>

        {/* Order Options */}
        <div className="order-options">
          <button className="option-pill" onClick={() => navigate("/")}>
            <span>＋</span> Add More Items
          </button>
          <button className="option-pill" onClick={() => setShowNote(!showNote)}>
            <span>📝</span> Note to Restaurant
          </button>
        </div>
        {showNote && (
          <textarea
            className="note-input"
            placeholder="Any special instructions..."
            value={note}
            onChange={(e) => setNote(e.target.value)}
            rows={3}
          />
        )}

        {/* Promo Code */}
        <div className="promo-section">
          <div className="promo-header">
            <span className="promo-icon">🏷️</span>
            <span className="promo-title">Apply Promo Code</span>
          </div>
          {promoCode ? (
            <div className="promo-applied">
              <span>✅ <strong>{promoCode}</strong> applied! You saved ৳{discount}</span>
              <button className="remove-promo" onClick={removePromo}>Remove</button>
            </div>
          ) : (
            <>
              <div className="promo-input-row">
                <input
                  className={`promo-input ${promoError ? "promo-error" : ""}`}
                  placeholder="Enter code"
                  value={promoInput}
                  onChange={(e) => { setPromoInput(e.target.value); setPromoError(""); }}
                  onKeyDown={(e) => e.key === "Enter" && handleApplyPromo()}
                />
                <button
                  className="apply-btn"
                  onClick={handleApplyPromo}
                  disabled={promoLoading}
                >
                  {promoLoading ? "..." : "Apply"}
                </button>
              </div>
              {promoError && <p className="promo-err-msg">{promoError}</p>}
              <p className="promo-hint">Try: FOOD20 or WELCOME50</p>
            </>
          )}
        </div>

        {/* Bill Summary */}
        <div className="bill-section">
          <h3 className="bill-title">Bill Summary</h3>
          <div className="bill-row">
            <span>Subtotal</span>
            <span>৳{subtotal}</span>
          </div>
          <div className="bill-row">
            <span>Delivery Fee</span>
            <span>৳{DELIVERY_FEE}</span>
          </div>
          {discount > 0 && (
            <div className="bill-row discount-row">
              <span>Discount ({promoCode})</span>
              <span>-৳{discount}</span>
            </div>
          )}
          <div className="bill-divider" />
          <div className="bill-row total-row">
            <span>Total</span>
            <span className="total-amount">৳{total}</span>
          </div>
        </div>
      </div>

      {/* Checkout Bar */}
      <div className="checkout-bar">
        <div className="checkout-total">
          <span className="checkout-label">Total Amount</span>
          <span className="checkout-price">৳{total}</span>
        </div>
        <button
          className="checkout-btn"
          onClick={handleCheckout}
          disabled={orderLoading}
        >
          {orderLoading ? "Placing..." : "Proceed to Checkout"}
        </button>
      </div>
    </div>
  );
}
