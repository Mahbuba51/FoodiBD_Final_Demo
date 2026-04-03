import { createContext, useContext, useReducer, useEffect } from "react";

const CartContext = createContext();

const cartReducer = (state, action) => {
  switch (action.type) {
    case "ADD_ITEM": {
      const existing = state.items.find(
        (item) =>
          item.id === action.payload.id &&
          JSON.stringify(item.customizations) ===
            JSON.stringify(action.payload.customizations)
      );
      if (existing) {
        return {
          ...state,
          items: state.items.map((item) =>
            item.id === existing.id &&
            JSON.stringify(item.customizations) ===
              JSON.stringify(existing.customizations)
              ? { ...item, quantity: item.quantity + action.payload.quantity }
              : item
          ),
        };
      }
      return { ...state, items: [...state.items, action.payload] };
    }
    case "REMOVE_ITEM":
      return {
        ...state,
        items: state.items.filter((_, index) => index !== action.payload),
      };
    case "UPDATE_QUANTITY":
      return {
        ...state,
        items: state.items.map((item, index) =>
          index === action.payload.index
            ? { ...item, quantity: action.payload.quantity }
            : item
        ),
      };
    case "CLEAR_CART":
      return { ...state, items: [] };
    case "APPLY_PROMO":
      // Store the promo type + value, NOT a fixed discount amount
      return {
        ...state,
        promoCode:     action.payload.code,
        promoType:     action.payload.type,   // "percent" | "flat"
        promoValue:    action.payload.value,  // 20 for FOOD20, 50 for WELCOME50
      };
    case "REMOVE_PROMO":
      return { ...state, promoCode: null, promoType: null, promoValue: 0 };
    default:
      return state;
  }
};

const initialState = {
  items:      JSON.parse(localStorage.getItem("cart_items") || "[]"),
  promoCode:  null,
  promoType:  null,
  promoValue: 0,
};

export const CartProvider = ({ children }) => {
  const [state, dispatch] = useReducer(cartReducer, initialState);

  useEffect(() => {
    localStorage.setItem("cart_items", JSON.stringify(state.items));
  }, [state.items]);

  const addItem       = (item)            => dispatch({ type: "ADD_ITEM",       payload: item });
  const removeItem    = (index)           => dispatch({ type: "REMOVE_ITEM",    payload: index });
  const updateQuantity = (index, quantity) => {
    if (quantity <= 0) {
      removeItem(index);
    } else {
      dispatch({ type: "UPDATE_QUANTITY", payload: { index, quantity } });
    }
  };
  const clearCart  = ()               => dispatch({ type: "CLEAR_CART" });
  const removePromo = ()              => dispatch({ type: "REMOVE_PROMO" });

  // applyPromo now receives type + value so discount can be recalculated live
  const applyPromo = (code, type, value) =>
    dispatch({ type: "APPLY_PROMO", payload: { code, type, value } });

  const DELIVERY_FEE = 50;

  const subtotal = state.items.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  // Recalculate discount from live subtotal every render
  const discount = state.promoCode
    ? state.promoType === "percent"
      ? Math.round((subtotal * state.promoValue) / 100)
      : state.promoValue
    : 0;

  const total      = Math.max(subtotal + DELIVERY_FEE - discount, 0);
  const totalItems = state.items.reduce((sum, item) => sum + item.quantity, 0);

  return (
    <CartContext.Provider
      value={{
        items:      state.items,
        promoCode:  state.promoCode,
        promoType:  state.promoType,
        promoValue: state.promoValue,
        discount,
        subtotal,
        total,
        totalItems,
        DELIVERY_FEE,
        addItem,
        removeItem,
        updateQuantity,
        clearCart,
        applyPromo,
        removePromo,
      }}
    >
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) throw new Error("useCart must be used within CartProvider");
  return context;
};
