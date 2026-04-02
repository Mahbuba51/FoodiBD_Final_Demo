import { menuItems, restaurant, PROMO_CODES } from "./mockData";

// ─── Restaurant & Menu ────────────────────────────────────────────────────────
export const getRestaurant = async () => {
  // TODO: Replace with → await fetch(`/api/restaurants/${id}`)
  return new Promise((resolve) => setTimeout(() => resolve(restaurant), 300));
};

export const getMenuItems = async () => {
  // TODO: Replace with → await fetch(`/api/restaurants/${id}/menu`)
  return new Promise((resolve) => setTimeout(() => resolve(menuItems), 300));
};

export const getMenuItemById = async (id) => {
  // TODO: Replace with → await fetch(`/api/menu-items/${id}`)
  const item = menuItems.find((m) => m.id === id);
  return new Promise((resolve) => setTimeout(() => resolve(item), 200));
};

// ─── Promo Code ───────────────────────────────────────────────────────────────
export const validatePromoCode = async (code, subtotal) => {
  // TODO: Replace with → await fetch(`/api/promo/validate`, { method: 'POST', body: ... })
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const promo = PROMO_CODES[code.toUpperCase()];
      if (!promo) {
        reject(new Error("Invalid promo code"));
        return;
      }
      const discount =
        promo.type === "percent"
          ? Math.round((subtotal * promo.discount) / 100)
          : promo.discount;
      resolve({ discount, label: promo.label });
    }, 500);
  });
};

// ─── Order / Cart ─────────────────────────────────────────────────────────────
export const placeOrder = async (cartItems, promoCode, total) => {
  // TODO: Replace with → await fetch(`/api/orders`, { method: 'POST', body: ... })
  const orderPayload = {
    items: cartItems,
    promoCode,
    total,
    timestamp: new Date().toISOString(),
  };
  console.log("Order placed:", orderPayload);
  return new Promise((resolve) =>
    setTimeout(() => resolve({ success: true, orderId: "ORD-" + Date.now() }), 800)
  );
};
