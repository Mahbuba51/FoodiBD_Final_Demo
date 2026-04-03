import { useState, useEffect } from "react";
import { useCart } from "../../context/CartContext";
import "./ItemModal.css";

export default function ItemModal({ item, onClose }) {
  const { addItem } = useCart();
  const [quantity, setQuantity] = useState(1);
  const [selected, setSelected] = useState({});
  const [toppings, setToppings] = useState([]);
  const [addedAnim, setAddedAnim] = useState(false);

  // Pre-select first option for each customization group
  useEffect(() => {
    const defaults = {};
    Object.entries(item.customizations || {}).forEach(([key, options]) => {
      if (key !== "toppings" && options.length > 0) {
        defaults[key] = options[0].name;
      }
    });
    setSelected(defaults);
  }, [item]);

  const toggleTopping = (name) => {
    const maxToppings = 9;
    if (toppings.includes(name)) {
      setToppings(toppings.filter((t) => t !== name));
    } else if (toppings.length < maxToppings) {
      setToppings([...toppings, name]);
    }
  };

  const getToppingPrice = (name) => {
    const t = item.customizations?.toppings?.find((t) => t.name === name);
    return t ? t.price : 0;
  };

  // Extra cost from selected options (size, bun, spice, etc.)
  const getSelectedOptionPrice = (key) => {
    const options = item.customizations?.[key];
    if (!options || key === "toppings") return 0;
    const chosen = options.find((o) => o.name === selected[key]);
    return chosen ? (chosen.price ?? 0) : 0;
  };

  const selectedExtraCost = Object.keys(selected).reduce(
    (sum, key) => sum + getSelectedOptionPrice(key),
    0
  );

  const toppingExtraCost = toppings.reduce(
    (sum, name) => sum + getToppingPrice(name),
    0
  );

  const extraCost  = selectedExtraCost + toppingExtraCost;
  const totalPrice = (item.price + extraCost) * quantity;

  const handleAdd = () => {
    const customizationSummary = Object.entries(selected)
      .map(([, v]) => v)
      .join(", ");

    addItem({
      id:                item.id,
      name:              item.name,
      image:             item.image,
      price:             item.price + extraCost,
      quantity,
      customizations:    { ...selected, toppings },
      customizationLabel: [customizationSummary, ...toppings].filter(Boolean).join(", "),
      restaurantName:    "Chillox - Lalbagh",
    });

    setAddedAnim(true);
    setTimeout(() => onClose(), 400);
  };

  return (
    <div className="modal-backdrop" onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={`modal-sheet ${addedAnim ? "modal-exit" : ""}`}>

        {/* Header image */}
        <div className="modal-img-wrap">
          <img src={item.image} alt={item.name} />
          <button className="modal-close" onClick={onClose}>✕</button>
        </div>

        <div className="modal-body">
          <h2 className="modal-title">{item.name}</h2>
          <p className="modal-from">From TK {item.price}</p>
          <p className="modal-desc">{item.fullDescription}</p>

          {/* Customization groups (bun, spice, size, etc.) */}
          {Object.entries(item.customizations || {}).map(([key, options]) => {
            if (key === "toppings") return null;
            const isRequired = options.some((o) => o.required);
            const label =
              key === "bun"        ? "Choose Your Bun" :
              key === "spiceLevel" ? "Spice Level" :
              key === "size"       ? "Size" : key;

            return (
              <div key={key} className="custom-group">
                <div className="group-header">
                  <span className="group-label">{label}</span>
                  <span className={`group-badge ${isRequired ? "required" : "completed"}`}>
                    {isRequired ? "Required" : "Completed"}
                  </span>
                </div>
                <p className="group-hint">Select one</p>
                {options.map((opt) => (
                  <div
                    key={opt.name}
                    className={`option-row ${selected[key] === opt.name ? "option-active" : ""}`}
                    onClick={() => setSelected({ ...selected, [key]: opt.name })}
                  >
                    <span className="option-name">{opt.name}</span>
                    <div className="option-right">
                      <span className="option-price">
                        {opt.price === 0 ? "Free" : `+৳${opt.price}`}
                      </span>
                      <div className={`radio-btn ${selected[key] === opt.name ? "radio-active" : ""}`} />
                    </div>
                  </div>
                ))}
              </div>
            );
          })}

          {/* Toppings / addons */}
          {item.customizations?.toppings && (
            <div className="custom-group">
              <div className="group-header">
                <span className="group-label">Extra Toppings</span>
                <span className="group-badge optional">Optional</span>
              </div>
              <p className="group-hint">Select up to 9</p>
              {item.customizations.toppings.map((t) => (
                <div
                  key={t.name}
                  className={`option-row ${toppings.includes(t.name) ? "option-active" : ""}`}
                  onClick={() => toggleTopping(t.name)}
                >
                  <div className="option-name-row">
                    <span className="topping-fire">🔥</span>
                    <span className="option-name">{t.name}</span>
                  </div>
                  <div className="option-right">
                    {t.originalPrice && (
                      <span className="original-price">TK {t.originalPrice}</span>
                    )}
                    <span className="option-price">TK {t.price}</span>
                    <div className={`checkbox-btn ${toppings.includes(t.name) ? "checkbox-active" : ""}`}>
                      {toppings.includes(t.name) && "✓"}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="modal-footer">
          <div className="qty-control">
            <button onClick={() => setQuantity(Math.max(1, quantity - 1))}>−</button>
            <span>{quantity}</span>
            <button onClick={() => setQuantity(quantity + 1)}>+</button>
          </div>
          <button
            className={`add-to-cart-main ${addedAnim ? "btn-success" : ""}`}
            onClick={handleAdd}
          >
            {addedAnim ? "Added! ✓" : `Add to cart - TK ${totalPrice}`}
          </button>
        </div>
      </div>
    </div>
  );
}
