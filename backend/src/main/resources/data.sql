TRUNCATE TABLE
  order_items,
  orders,
  customization_options,
  customizations,
  addons,
  menu_item_categories,
  menu_items,
  menus,
  restaurant_cuisine_types,
  customers,
  restaurants
RESTART IDENTITY CASCADE;

-- ─── Restaurant ──────────────────────────────────────────────────────────────
INSERT INTO restaurants (name, rating, available,
    address_street, address_city, address_state, address_postal_code, address_country)
VALUES ('Chillox - Lalbagh', 3.9, true,
    'Lalbagh Road 12', 'Dhaka', 'Dhaka Division', '1211', 'Bangladesh');

-- ─── Cuisine types ────────────────────────────────────────────────────────────
INSERT INTO restaurant_cuisine_types (restaurant_id, cuisine_type) VALUES (1, 'Burger');
INSERT INTO restaurant_cuisine_types (restaurant_id, cuisine_type) VALUES (1, 'Fast Food');

-- ─── Menu ─────────────────────────────────────────────────────────────────────
INSERT INTO menus (restaurant_id) VALUES (1);

-- ─── Menu items ───────────────────────────────────────────────────────────────
INSERT INTO menu_items (name, description, price, availability, is_popular, thumbnail_url, menu_id)
VALUES
  ('Classic Chicken Burger',
   'Juicy chicken patty with cheese, lettuce, tomato',
   188, true, true,
   'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&h=300&fit=crop', 1),

  ('Fish Burger',
   'Crispy fish fillet with tartar sauce',
   220, true, true,
   'https://images.unsplash.com/photo-1553979459-d2229ba7433b?w=400&h=300&fit=crop', 1),

  ('Spicy Wings Combo',
   '6 spicy wings with dipping sauce and fries',
   420, true, false,
   'https://images.unsplash.com/photo-1527477396000-e27163b481c2?w=400&h=300&fit=crop', 1),

  ('Cheese Fries',
   'Golden fries loaded with melted cheese',
   180, true, false,
   'https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=400&h=300&fit=crop', 1),

  ('Onion Rings',
   'Crispy golden onion rings',
   150, true, false,
   'https://images.unsplash.com/photo-1639024471283-03518883512d?w=400&h=300&fit=crop', 1),

  ('Chocolate Shake',
   'Rich and creamy chocolate milkshake',
   220, true, false,
   'https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=400&h=300&fit=crop', 1);

-- ─── Menu item categories ─────────────────────────────────────────────────────
INSERT INTO menu_item_categories (item_id, category) VALUES
  (1, 'Burgers'), (2, 'Burgers'),
  (3, 'Combos'),
  (4, 'Sides'), (5, 'Sides'),
  (6, 'Drinks');

-- ─── Customizations ───────────────────────────────────────────────────────────
-- Classic Chicken Burger: bun + spice
INSERT INTO customizations (description, menu_item_id) VALUES
  ('Choose Your Bun', 1),
  ('Spice Level',     1),
  ('Spice Level',     2),
  ('Spice Level',     3),
  ('Size',            4),
  ('Size',            6);

-- ─── Customization options ────────────────────────────────────────────────────
-- Bun (customization_id = 1)
INSERT INTO customization_options (option_name, extra_price, is_default, customization_id) VALUES
  ('Brioche Bun', 0, true,  1),
  ('Sesame Bun',  0, false, 1);

-- Spice — Chicken Burger (customization_id = 2)
INSERT INTO customization_options (option_name, extra_price, is_default, customization_id) VALUES
  ('Mild',         0, true,  2),
  ('Extreme Naga', 0, false, 2);

-- Spice — Fish Burger (customization_id = 3)
INSERT INTO customization_options (option_name, extra_price, is_default, customization_id) VALUES
  ('Mild', 0, true,  3),
  ('Hot',  0, false, 3);

-- Spice — Wings Combo (customization_id = 4)
INSERT INTO customization_options (option_name, extra_price, is_default, customization_id) VALUES
  ('Mild',      0, false, 4),
  ('Hot',       0, true,  4),
  ('Extra Hot', 0, false, 4);

-- Size — Cheese Fries (customization_id = 5)
INSERT INTO customization_options (option_name, extra_price, is_default, customization_id) VALUES
  ('Regular', 0,  true,  5),
  ('Large',   30, false, 5);

-- Size — Chocolate Shake (customization_id = 6)
INSERT INTO customization_options (option_name, extra_price, is_default, customization_id) VALUES
  ('Regular', 0,  true,  6),
  ('Large',   40, false, 6);

-- ─── Addons (toppings) ────────────────────────────────────────────────────────
-- Classic Chicken Burger (item_id = 1)
INSERT INTO addons (name, price, is_popular, menu_item_id) VALUES
  ('BBQ Sauce',     28,  false, 1),
  ('Egg',           28,  false, 1),
  ('Beef Bacon',    72,  true,  1),
  ('Honey BBQ',     32,  false, 1),
  ('Sausage',       48,  false, 1),
  ('Chicken Patty', 120, true,  1);

-- Fish Burger (item_id = 2)
INSERT INTO addons (name, price, is_popular, menu_item_id) VALUES
  ('Extra Cheese', 32, false, 2),
  ('Jalapeños',    24, false, 2);

-- ─── Demo customer ────────────────────────────────────────────────────────────
INSERT INTO customers (name, email, phone_number,
    address_street, address_city, address_state, address_postal_code, address_country)
VALUES ('Demo User', 'demo@foodi.bd', '01700000000',
    'Road 5, House 12', 'Dhaka', 'Dhaka Division', '1209', 'Bangladesh');
