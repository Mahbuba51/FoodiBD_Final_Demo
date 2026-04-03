const API_BASE = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api";

const RESTAURANT_ID = 1;
const DELIVERY_FEE  = 50;

// ─── Restaurant & Menu ────────────────────────────────────────────────────────

export const getRestaurant = async () => {
  const res = await fetch(`${API_BASE}/restaurants/${RESTAURANT_ID}`);
  if (!res.ok) throw new Error("Failed to fetch restaurant");
  const data = await res.json();
  return {
    id:                data.restaurantId,
    name:              data.name,
    category:          data.cuisineTags?.join(", ") ?? "",
    rating:            data.rating,
    reviews:           data.ratingCount,
    distance:          `${data.distanceM} m`,
    deliveryTime:      `${data.deliveryTimeMin}-${data.deliveryTimeMax} min`,
    deliveryBy:        data.deliveredBy,
    minOrder:          data.deliveryFee,
    estimatedDelivery: `${data.deliveryTimeMin}-${data.deliveryTimeMax} mins`,
    isOpen:            data.isOpen,
  };
};

export const getMenuItems = async () => {
  const res = await fetch(`${API_BASE}/restaurants/${RESTAURANT_ID}/menu`);
  if (!res.ok) throw new Error("Failed to fetch menu");
  const data = await res.json();
  const items = [];
  for (const cat of (data.categories ?? [])) {
    for (const item of (cat.items ?? [])) {
      items.push({
        id:           item.itemId,
        name:         item.name,
        category:     cat.categoryName,
        description:  item.description,
        price:        item.price,
        displayPrice: item.price,
        image:        item.thumbnailUrl,
        isPopular:    item.isPopular,
      });
    }
  }
  return items;
};

export const getMenuItemById = async (id) => {
  const res = await fetch(`${API_BASE}/restaurants/${RESTAURANT_ID}/menu/items/${id}`);
  if (!res.ok) throw new Error("Failed to fetch menu item");
  const data = await res.json();

  const customizations = {};
  for (const c of (data.customizations ?? [])) {
    const key = descriptionToKey(c.description);
    customizations[key] = (c.options ?? []).map(o => ({
      name:     o.optionName,
      price:    o.extraPrice,
      required: o.isDefault,
    }));
  }

  if (data.addons?.length) {
    customizations.toppings = data.addons.map(a => ({
      name:  a.name,
      price: a.price,
    }));
  }

  return {
    id:              data.itemId,
    name:            data.name,
    description:     data.description,
    fullDescription: data.description,
    price:           data.basePrice,
    displayPrice:    data.basePrice,
    image:           data.thumbnailUrl,
    isPopular:       data.isPopular,
    customizations,
  };
};

// ─── Promo Code ───────────────────────────────────────────────────────────────

export const validatePromoCode = async (code, subtotal) => {
  const res = await fetch(`${API_BASE}/cart/promo`, {
    method:  "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      promo_code:    code,
      restaurant_id: RESTAURANT_ID,
      subtotal,
      delivery_fee:  DELIVERY_FEE,
    }),
  });

  const data = await res.json();

  if (!data.is_valid) {
    throw new Error(data.message ?? "Invalid promo code");
  }

  // Return type + value so CartContext can recalculate discount live
  // backend returns discount_type as "percentage" or "flat"
  return {
    type:  data.discount_type === "percentage" ? "percent" : "flat",
    value: data.discount_value,  // 20 for FOOD20, 50 for WELCOME50
    label: `${data.discount_value}${data.discount_type === "percentage" ? "%" : "৳"} off`,
  };
};

// ─── Order / Checkout ─────────────────────────────────────────────────────────

export const placeOrder = async (cartItems, promoCode, total) => {
  const verifyRes = await fetch(`${API_BASE}/cart/verify`, {
    method:  "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      restaurant_id: RESTAURANT_ID,
      items:         cartItemsToDTO(cartItems),
      promo_code:    promoCode ?? null,
    }),
  });

  const verifyData = await verifyRes.json();
  if (verifyData.message !== "Cart verified successfully") {
    return { success: false, message: verifyData.message };
  }

  // Simulate order placement
  console.log("Order placed:", { cartItems, promoCode, total });
  return { success: true, orderId: "ORD-" + Date.now() };
};

// ─── Helpers ──────────────────────────────────────────────────────────────────

function cartItemsToDTO(cartItems) {
  return cartItems.map(item => ({
    item_id:              item.id,
    quantity:             item.quantity,
    customizations:       [],
    addon_ids:            [],
    special_instructions: "",
  }));
}

function descriptionToKey(description) {
  const map = {
    "Choose Your Bun": "bun",
    "Spice Level":     "spiceLevel",
    "Size":            "size",
  };
  return map[description] ?? description.toLowerCase().replace(/\s+/g, "_");
}
