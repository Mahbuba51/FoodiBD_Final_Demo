#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# Edge-case test suite for FoodiBD backend
# Run:  chmod +x edge_case_tests.sh && ./edge_case_tests.sh
# Requires: curl, jq
# ─────────────────────────────────────────────────────────────────────────────

BASE="http://localhost:8080"
PASS=0
FAIL=0

# ── helpers ──────────────────────────────────────────────────────────────────

check() {
  local label="$1"
  local expected_status="$2"
  local expected_fragment="$3"   # substring that must appear in the body
  local actual_status="$4"
  local actual_body="$5"

  local status_ok=false
  local body_ok=false

  [[ "$actual_status" == "$expected_status" ]] && status_ok=true
  [[ "$actual_body"   == *"$expected_fragment"* ]] && body_ok=true

  if $status_ok && $body_ok; then
    echo "  ✅ PASS — $label"
    ((PASS++))
  else
    echo "  ❌ FAIL — $label"
    $status_ok || echo "       status: expected=$expected_status  got=$actual_status"
    $body_ok   || echo "       body fragment «$expected_fragment» not found in: $actual_body"
    ((FAIL++))
  fi
}

req() {
  # req METHOD URL [extra curl args...]
  local method="$1"; shift
  local url="$1";    shift
  curl -s -o /tmp/_body -w "%{http_code}" -X "$method" "$url" "$@"
}

# ─────────────────────────────────────────────────────────────────────────────
echo ""
echo "════════════════════════════════════════════"
echo " RESTAURANT — not-found (404) cases"
echo "════════════════════════════════════════════"

# T-R1  Non-existent restaurant
status=$(req GET "$BASE/restaurants/99999")
body=$(cat /tmp/_body)
check "GET /restaurants/99999 → 404 + message" \
  "404" "not found" "$status" "$body"

# T-R2  Non-existent menu (restaurant exists but hypothetically has no menu)
#       The service throws NoSuchElementException("Menu not found for restaurant:…")
#       Use a valid restaurant that has no menu seeded (restaurant_id=999 is safe)
status=$(req GET "$BASE/restaurants/99999/menu")
body=$(cat /tmp/_body)
check "GET /restaurants/99999/menu → 404 + message" \
  "404" "not found" "$status" "$body"

# T-R3  Non-existent restaurant when fetching a menu item
status=$(req GET "$BASE/restaurants/99999/menu/items/1")
body=$(cat /tmp/_body)
check "GET /restaurants/99999/menu/items/1 → 404 + message" \
  "404" "not found" "$status" "$body"

# T-R4  Valid restaurant, non-existent item
status=$(req GET "$BASE/restaurants/1/menu/items/99999")
body=$(cat /tmp/_body)
check "GET /restaurants/1/menu/items/99999 → 404 + message" \
  "404" "not found" "$status" "$body"

# ─────────────────────────────────────────────────────────────────────────────
echo ""
echo "════════════════════════════════════════════"
echo " RESTAURANT MENU — search query filtering"
echo "════════════════════════════════════════════"

# T-R5  Query that matches nothing → 200, empty categories list
status=$(req GET "$BASE/restaurants/1/menu?query=xyznonexistent")
body=$(cat /tmp/_body)
check "GET /restaurants/1/menu?query=xyznonexistent → 200 + empty categories" \
  "200" "categories" "$status" "$body"

# T-R6  Query that matches a known item name (partial, case-insensitive)
status=$(req GET "$BASE/restaurants/1/menu?query=burger")
body=$(cat /tmp/_body)
check "GET /restaurants/1/menu?query=burger → 200 + results" \
  "200" "burger" "$status" "$body"

# ─────────────────────────────────────────────────────────────────────────────
echo ""
echo "════════════════════════════════════════════"
echo " CART / VERIFY — all 200 error-message cases"
echo "════════════════════════════════════════════"

# T-V1  Empty items array
status=$(req POST "$BASE/cart/verify" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"restaurant_id":1,"items":[]}')
body=$(cat /tmp/_body)
check "POST /cart/verify  empty items → 200 + 'Cart is empty.'" \
  "200" "Cart is empty." "$status" "$body"

# T-V2  Null items (omit the field entirely)
status=$(req POST "$BASE/cart/verify" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"restaurant_id":1}')
body=$(cat /tmp/_body)
check "POST /cart/verify  null items → 200 + 'Cart is empty.'" \
  "200" "Cart is empty." "$status" "$body"

# T-V3  Non-existent restaurant
status=$(req POST "$BASE/cart/verify" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"restaurant_id":99999,"items":[{"item_id":1,"quantity":1}]}')
body=$(cat /tmp/_body)
check "POST /cart/verify  bad restaurant → 200 + 'Restaurant not found.'" \
  "200" "Restaurant not found." "$status" "$body"

# T-V4  Non-existent item
status=$(req POST "$BASE/cart/verify" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"restaurant_id":1,"items":[{"item_id":99999,"quantity":1}]}')
body=$(cat /tmp/_body)
check "POST /cart/verify  bad item → 200 + 'not found.'" \
  "200" "not found." "$status" "$body"

# T-V5  Quantity = 0
status=$(req POST "$BASE/cart/verify" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"restaurant_id":1,"items":[{"item_id":1,"quantity":0}]}')
body=$(cat /tmp/_body)
check "POST /cart/verify  quantity=0 → 200 + 'Invalid quantity'" \
  "200" "Invalid quantity" "$status" "$body"

# T-V6  Negative quantity
status=$(req POST "$BASE/cart/verify" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"restaurant_id":1,"items":[{"item_id":1,"quantity":-3}]}')
body=$(cat /tmp/_body)
check "POST /cart/verify  quantity=-3 → 200 + 'Invalid quantity'" \
  "200" "Invalid quantity" "$status" "$body"

# ─────────────────────────────────────────────────────────────────────────────
echo ""
echo "════════════════════════════════════════════"
echo " CART / BILL — all 200 error-message cases"
echo "════════════════════════════════════════════"

# T-B1  Empty items array
status=$(req POST "$BASE/cart/bill" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"restaurant_id":1,"items":[]}')
body=$(cat /tmp/_body)
check "POST /cart/bill  empty items → 200 + 'Cart is empty.' + total=50" \
  "200" "Cart is empty." "$status" "$body"

# T-B2  Empty items: total must equal DELIVERY_FEE only (50)
check "POST /cart/bill  empty items → total field equals delivery fee" \
  "200" '"total":50' "$status" "$body"

# T-B3  Invalid promo code
status=$(req POST "$BASE/cart/bill" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"restaurant_id":1,"items":[{"item_id":1,"quantity":2}],"promo_code":"BADCODE"}')
body=$(cat /tmp/_body)
check "POST /cart/bill  bad promo → 200 + 'Invalid promo code. Full price charged.'" \
  "200" "Invalid promo code. Full price charged." "$status" "$body"

# T-B4  Invalid promo: promo_applied must be false
check "POST /cart/bill  bad promo → promo_applied=false" \
  "200" '"promo_applied":false' "$status" "$body"

# T-B5  FOODI50 flat discount (50 off regardless of subtotal)
status=$(req POST "$BASE/cart/bill" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"restaurant_id":1,"items":[{"item_id":1,"quantity":2}],"promo_code":"FOODI50"}')
body=$(cat /tmp/_body)
check "POST /cart/bill  FOODI50 → 200 + discount=50" \
  "200" '"discount":50' "$status" "$body"

# T-B6  WELCOME promo (15% discount)
status=$(req POST "$BASE/cart/bill" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"restaurant_id":1,"items":[{"item_id":1,"quantity":2}],"promo_code":"WELCOME"}')
body=$(cat /tmp/_body)
check "POST /cart/bill  WELCOME → 200 + promo_applied=true" \
  "200" '"promo_applied":true' "$status" "$body"

# T-B7  Promo code is whitespace only (treated as no promo — no discount)
status=$(req POST "$BASE/cart/bill" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"restaurant_id":1,"items":[{"item_id":1,"quantity":2}],"promo_code":"   "}')
body=$(cat /tmp/_body)
check "POST /cart/bill  blank promo → 200 + promo_applied=false" \
  "200" '"promo_applied":false' "$status" "$body"

# T-B8  No promo field at all
status=$(req POST "$BASE/cart/bill" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"restaurant_id":1,"items":[{"item_id":1,"quantity":2}]}')
body=$(cat /tmp/_body)
check "POST /cart/bill  omitted promo → 200 + promo_applied=false" \
  "200" '"promo_applied":false' "$status" "$body"

# ─────────────────────────────────────────────────────────────────────────────
echo ""
echo "════════════════════════════════════════════"
echo " CART / PROMO — all 200 error-message cases"
echo "════════════════════════════════════════════"

# T-P1  Null / empty promo code
status=$(req POST "$BASE/cart/promo" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"promo_code":"","restaurant_id":1,"subtotal":640}')
body=$(cat /tmp/_body)
check "POST /cart/promo  empty code → 200 + 'No promo code provided.'" \
  "200" "No promo code provided." "$status" "$body"

# T-P2  is_valid must be false
check "POST /cart/promo  empty code → is_valid=false" \
  "200" '"is_valid":false' "$status" "$body"

# T-P3  Invalid promo code
status=$(req POST "$BASE/cart/promo" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"promo_code":"FAKEPROMO","restaurant_id":1,"subtotal":640}')
body=$(cat /tmp/_body)
check "POST /cart/promo  bad code → 200 + 'Invalid or expired promo code.'" \
  "200" "Invalid or expired promo code." "$status" "$body"

# T-P4  Invalid promo: is_valid=false
check "POST /cart/promo  bad code → is_valid=false" \
  "200" '"is_valid":false' "$status" "$body"

# T-P5  FOODI10 case-insensitive (lowercase)
status=$(req POST "$BASE/cart/promo" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"promo_code":"foodi10","restaurant_id":1,"subtotal":640}')
body=$(cat /tmp/_body)
check "POST /cart/promo  foodi10 (lowercase) → 200 + is_valid=true" \
  "200" '"is_valid":true' "$status" "$body"

# T-P6  FOODI50 flat — discount_amount should always be 50 regardless of subtotal
status=$(req POST "$BASE/cart/promo" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"promo_code":"FOODI50","restaurant_id":1,"subtotal":100}')
body=$(cat /tmp/_body)
check "POST /cart/promo  FOODI50 → discount_amount=50" \
  "200" '"discount_amount":50' "$status" "$body"

# T-P7  WELCOME — 15% of subtotal=200 = 30
status=$(req POST "$BASE/cart/promo" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"promo_code":"WELCOME","restaurant_id":1,"subtotal":200}')
body=$(cat /tmp/_body)
check "POST /cart/promo  WELCOME subtotal=200 → discount_amount=30" \
  "200" '"discount_amount":30' "$status" "$body"

# T-P8  Subtotal = 0 (no items priced yet) — should not crash
status=$(req POST "$BASE/cart/promo" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"promo_code":"FOODI10","restaurant_id":1,"subtotal":0}')
body=$(cat /tmp/_body)
check "POST /cart/promo  subtotal=0 → 200 + discount_amount=0" \
  "200" '"discount_amount":0' "$status" "$body"

# T-P9  Subtotal field omitted entirely (service defaults to 0)
status=$(req POST "$BASE/cart/promo" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{"promo_code":"FOODI10","restaurant_id":1}')
body=$(cat /tmp/_body)
check "POST /cart/promo  omitted subtotal → 200 (no crash)" \
  "200" '"is_valid":true' "$status" "$body"

# ─────────────────────────────────────────────────────────────────────────────
echo ""
echo "════════════════════════════════════════════"
echo " MISSING / MALFORMED AUTH HEADER"
echo "════════════════════════════════════════════"

# T-A1  /cart/verify without Authorization header → Spring returns 400
status=$(req POST "$BASE/cart/verify" \
  -H "Content-Type: application/json" \
  -d '{"restaurant_id":1,"items":[{"item_id":1,"quantity":1}]}')
body=$(cat /tmp/_body)
check "POST /cart/verify  no Auth header → 400 Bad Request" \
  "400" "" "$status" "$body"

# T-A2  /cart/bill without Authorization header
status=$(req POST "$BASE/cart/bill" \
  -H "Content-Type: application/json" \
  -d '{"restaurant_id":1,"items":[{"item_id":1,"quantity":1}]}')
body=$(cat /tmp/_body)
check "POST /cart/bill  no Auth header → 400 Bad Request" \
  "400" "" "$status" "$body"

# T-A3  /cart/promo without Authorization header
status=$(req POST "$BASE/cart/promo" \
  -H "Content-Type: application/json" \
  -d '{"promo_code":"FOODI10","subtotal":200}')
body=$(cat /tmp/_body)
check "POST /cart/promo  no Auth header → 400 Bad Request" \
  "400" "" "$status" "$body"

# ─────────────────────────────────────────────────────────────────────────────
echo ""
echo "════════════════════════════════════════════"
echo " SUMMARY"
echo "════════════════════════════════════════════"
echo "  Passed : $PASS"
echo "  Failed : $FAIL"
echo "  Total  : $((PASS + FAIL))"
echo ""
[[ $FAIL -eq 0 ]] && exit 0 || exit 1
