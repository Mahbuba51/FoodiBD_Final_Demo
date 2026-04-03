import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { getRestaurant, getMenuItems, getMenuItemById } from "../services/cartService";
import { useCart } from "../context/CartContext";
import ItemModal from "../components/cart/ItemModal";
import "./MenuPage.css";

export default function MenuPage() {
  const [restaurant, setRestaurant] = useState(null);
  const [menuItems, setMenuItems] = useState([]);
  const [activeCategory, setActiveCategory] = useState("");
  const [selectedItem, setSelectedItem] = useState(null);
  const [loadingItemId, setLoadingItemId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const { totalItems } = useCart();
  const navigate = useNavigate();

  const sectionRefs = useRef({});
  const tabsRef = useRef(null);

  useEffect(() => {
    const load = async () => {
      const [r, m] = await Promise.all([getRestaurant(), getMenuItems()]);
      setRestaurant(r);
      setMenuItems(m);
      setLoading(false);
    };
    load();
  }, []);

  // ── Search filtering ───────────────────────────────────────────────────────
  const isSearching = searchQuery.trim().length > 0;
  const q = searchQuery.toLowerCase().trim();

  const filteredItems = isSearching
    ? menuItems.filter(
        (item) =>
          item.name.toLowerCase().includes(q) ||
          item.description.toLowerCase().includes(q) ||
          item.category.toLowerCase().includes(q)
      )
    : menuItems;

  const categories = [...new Set(menuItems.map((i) => i.category))];

  // ── Sticky tab scroll tracking (only when not searching) ──────────────────
  useEffect(() => {
    if (categories.length > 0 && !activeCategory) {
      setActiveCategory(categories[0]);
    }
  }, [categories]);

  useEffect(() => {
    if (isSearching) return;
    const handleScroll = () => {
      for (const cat of categories) {
        const el = sectionRefs.current[cat];
        if (el) {
          const rect = el.getBoundingClientRect();
          if (rect.top <= 150 && rect.bottom > 150) {
            setActiveCategory(cat);
            break;
          }
        }
      }
    };
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [categories, isSearching]);

  const scrollToCategory = (cat) => {
    setActiveCategory(cat);
    const el = sectionRefs.current[cat];
    if (el) {
      const offset = 120;
      const top = el.getBoundingClientRect().top + window.scrollY - offset;
      window.scrollTo({ top, behavior: "smooth" });
    }
    const tabEl = tabsRef.current?.querySelector(`[data-cat="${cat}"]`);
    if (tabEl) {
      tabEl.scrollIntoView({ behavior: "smooth", block: "nearest", inline: "center" });
    }
  };

  // ── Open modal with full item details ─────────────────────────────────────
  const handleAddToCart = async (item) => {
    setLoadingItemId(item.id);
    try {
      const fullItem = await getMenuItemById(item.id);
      setSelectedItem(fullItem);
    } catch {
      setSelectedItem(item);
    } finally {
      setLoadingItemId(null);
    }
  };

  if (loading) return <div className="loading-screen"><div className="spinner" /></div>;

  return (
    <div className="menu-page">
      {/* Hero */}
      <div className="menu-hero">
        <button className="back-btn" onClick={() => navigate(-1)}>
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
            <path d="M19 12H5M12 5l-7 7 7 7" />
          </svg>
        </button>
        <div className="hero-img-wrap">
          <img src="https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&h=300&fit=crop" alt="chillox" />
          <div className="hero-overlay" />
          <span className="brand-name">chillox</span>
        </div>
        <button className="cart-fab" onClick={() => navigate("/cart")}>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2">
            <path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4z" />
            <line x1="3" y1="6" x2="21" y2="6" />
            <path d="M16 10a4 4 0 01-8 0" />
          </svg>
          {totalItems > 0 && <span className="cart-badge">{totalItems}</span>}
        </button>
      </div>

      {/* Restaurant info */}
      <div className="restaurant-info">
        <h1>{restaurant?.name}</h1>
        <p className="cat-label">{restaurant?.category}</p>
        <div className="meta-row">
          <span>⭐ {restaurant?.rating} ({restaurant?.reviews})</span>
          <span>📍 {restaurant?.distance}</span>
        </div>
        <div className="meta-row">
          <span>🕐 {restaurant?.deliveryTime}</span>
          <span>🛵 {restaurant?.deliveryBy}</span>
          <span>৳{restaurant?.minOrder}</span>
        </div>
      </div>

      {/* Search */}
      <div className="search-wrap">
        <svg className="search-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#aaa" strokeWidth="2">
          <circle cx="11" cy="11" r="8" /><path d="M21 21l-4.35-4.35" />
        </svg>
        <input
          placeholder="Search in menu..."
          className="search-input"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
        {isSearching && (
          <button className="search-clear" onClick={() => setSearchQuery("")}>✕</button>
        )}
      </div>

      {/* Category tabs — hidden while searching */}
      {!isSearching && (
        <div className="category-tabs sticky-tabs" ref={tabsRef}>
          {categories.map((cat) => (
            <button
              key={cat}
              data-cat={cat}
              className={`cat-tab ${activeCategory === cat ? "active" : ""}`}
              onClick={() => scrollToCategory(cat)}
            >
              {cat}
            </button>
          ))}
        </div>
      )}

      {/* Menu list */}
      <div className="menu-list">

        {/* Search results view */}
        {isSearching && (
          <>
            <p className="search-results-label">
              {filteredItems.length === 0
                ? `No items found for "${searchQuery}"`
                : `${filteredItems.length} result${filteredItems.length !== 1 ? "s" : ""} for "${searchQuery}"`}
            </p>
            {filteredItems.map((item) => (
              <div key={item.id} className="menu-card">
                <div className="menu-card-content">
                  <img src={item.image} alt={item.name} className="menu-item-img" />
                  <div className="menu-item-info">
                    <div className="item-name-row">
                      <span className="item-name">{item.name}</span>
                      {item.isPopular && <span className="popular-badge">🔥 Popular</span>}
                    </div>
                    <p className="item-desc">{item.description}</p>
                    <div className="item-price-row">
                      <span className="item-category-tag">{item.category}</span>
                      <span className="item-price">৳{item.displayPrice}</span>
                    </div>
                  </div>
                </div>
                <button
                  className="add-cart-btn"
                  onClick={() => handleAddToCart(item)}
                  disabled={loadingItemId === item.id}
                >
                  {loadingItemId === item.id ? "Loading..." : "Add to Cart"}
                </button>
              </div>
            ))}
          </>
        )}

        {/* Normal category view */}
        {!isSearching && categories.map((cat) => (
          <div key={cat} ref={(el) => (sectionRefs.current[cat] = el)}>
            <h2 className="section-title">{cat}</h2>
            {menuItems
              .filter((item) => item.category === cat)
              .map((item) => (
                <div key={item.id} className="menu-card">
                  <div className="menu-card-content">
                    <img src={item.image} alt={item.name} className="menu-item-img" />
                    <div className="menu-item-info">
                      <div className="item-name-row">
                        <span className="item-name">{item.name}</span>
                        {item.isPopular && <span className="popular-badge">🔥 Popular</span>}
                      </div>
                      <p className="item-desc">{item.description}</p>
                      <span className="item-price">৳{item.displayPrice}</span>
                    </div>
                  </div>
                  <button
                    className="add-cart-btn"
                    onClick={() => handleAddToCart(item)}
                    disabled={loadingItemId === item.id}
                  >
                    {loadingItemId === item.id ? "Loading..." : "Add to Cart"}
                  </button>
                </div>
              ))}
          </div>
        ))}
      </div>

      {/* Bottom cart bar */}
      {totalItems > 0 && (
        <div className="bottom-cart-bar" onClick={() => navigate("/cart")}>
          <span className="bar-count">{totalItems} item{totalItems > 1 ? "s" : ""}</span>
          <span>View Cart</span>
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2.5">
            <path d="M5 12h14M12 5l7 7-7 7" />
          </svg>
        </div>
      )}

      {/* Item modal */}
      {selectedItem && (
        <ItemModal item={selectedItem} onClose={() => setSelectedItem(null)} />
      )}
    </div>
  );
}
