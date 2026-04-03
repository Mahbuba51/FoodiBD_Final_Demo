export const restaurant = {
  id: 1,
  name: "Chillox - Lalbagh",
  category: "Burger, Fast Food",
  rating: 3.9,
  reviews: 557,
  distance: "509 m",
  deliveryTime: "25-40 min",
  deliveryBy: "Delivered by Foodi",
  minOrder: 31,
  address: "Home - Dhanmondi 27, Road 5, House 12, Dhaka 1209",
  estimatedDelivery: "25-30 mins",
};

export const menuCategories = ["Burgers", "Combos", "Sides", "Drinks"];

export const menuItems = [
  {
    id: 1,
    name: "Classic Chicken Burger",
    category: "Burgers",
    description: "Juicy chicken patty with cheese, lettuce, tomato",
    fullDescription:
      "Tender chicken patty, fresh lettuce, ripe tomatoes, and tangy sauce create a mouthwatering, satisfying burger experience.",
    price: 188,
    displayPrice: 450,
    image: "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&h=300&fit=crop",
    isPopular: true,
    customizations: {
      bun: [
        { name: "Brioche Bun", price: 0 },
        { name: "Sesame Bun", price: 0 },
      ],
      spiceLevel: [
        { name: "Mild", price: 0, required: true },
        { name: "Extreme Naga", price: 0 },
      ],
      toppings: [
        { name: "BBQ Sauce", originalPrice: 35, price: 28 },
        { name: "Egg", originalPrice: 35, price: 28 },
        { name: "Beef Bacon", originalPrice: 90, price: 72 },
        { name: "Honey BBQ", originalPrice: 40, price: 32 },
        { name: "Sausage", originalPrice: 60, price: 48 },
        { name: "Chicken Patty", originalPrice: 159, price: 120 },
      ],
    },
  },
  {
    id: 2,
    name: "Fish Burger",
    category: "Burgers",
    description: "Crispy fish fillet with tartar sauce",
    fullDescription: "Crispy golden fish fillet topped with fresh tartar sauce and crispy lettuce.",
    price: 220,
    displayPrice: 220,
    image: "https://images.unsplash.com/photo-1553979459-d2229ba7433b?w=400&h=300&fit=crop",
    isPopular: true,
    customizations: {
      spiceLevel: [
        { name: "Mild", price: 0, required: true },
        { name: "Hot", price: 0 },
      ],
      toppings: [
        { name: "Extra Cheese", originalPrice: 40, price: 32 },
        { name: "Jalapeños", originalPrice: 30, price: 24 },
      ],
    },
  },
  {
    id: 3,
    name: "Spicy Wings Combo",
    category: "Combos",
    description: "6 spicy wings with dipping sauce and fries",
    fullDescription: "Six crispy spicy wings served with signature dipping sauce and golden fries.",
    price: 420,
    displayPrice: 420,
    image: "https://images.unsplash.com/photo-1527477396000-e27163b481c2?w=400&h=300&fit=crop",
    isPopular: false,
    customizations: {
      spiceLevel: [
        { name: "Mild", price: 0 },
        { name: "Hot", price: 0, required: true },
        { name: "Extra Hot", price: 0 },
      ],
    },
  },
  {
    id: 4,
    name: "Cheese Fries",
    category: "Sides",
    description: "Golden fries loaded with melted cheese",
    fullDescription: "Crispy golden fries generously loaded with rich, melted cheese sauce.",
    price: 180,
    displayPrice: 180,
    image: "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=400&h=300&fit=crop",
    isPopular: false,
    customizations: {
      size: [
        { name: "Regular", price: 0 },
        { name: "Large", price: 30 },
      ],
    },
  },
  {
    id: 5,
    name: "Onion Rings",
    category: "Sides",
    description: "Crispy golden onion rings",
    fullDescription: "Hand-battered crispy golden onion rings served with dipping sauce.",
    price: 150,
    displayPrice: 150,
    image: "https://images.unsplash.com/photo-1639024471283-03518883512d?w=400&h=300&fit=crop",
    isPopular: false,
    customizations: {},
  },
  {
    id: 6,
    name: "Chocolate Shake",
    category: "Drinks",
    description: "Rich and creamy chocolate milkshake",
    fullDescription: "Thick and creamy chocolate milkshake made with real cocoa and premium ice cream.",
    price: 220,
    displayPrice: 220,
    image: "https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=400&h=300&fit=crop",
    isPopular: false,
    customizations: {
      size: [
        { name: "Regular", price: 0 },
        { name: "Large", price: 40 },
      ],
    },
  },
];

export const PROMO_CODES = {
  FOOD20: { discount: 20, type: "percent", label: "20% off" },
  WELCOME50: { discount: 50, type: "flat", label: "৳50 off" },
};
