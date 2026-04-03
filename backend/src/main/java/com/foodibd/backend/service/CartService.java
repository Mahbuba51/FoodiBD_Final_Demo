package com.foodibd.backend.service;

import com.foodibd.backend.dto.cart.bill.BillRequestDTO;
import com.foodibd.backend.dto.cart.bill.BillResponseDTO;
import com.foodibd.backend.dto.cart.item.CartItemDTO;
import com.foodibd.backend.dto.cart.promo.PromoRequestDTO;
import com.foodibd.backend.dto.cart.promo.PromoResponseDTO;
import com.foodibd.backend.dto.cart.verify.VerifyRequestDTO;
import com.foodibd.backend.dto.cart.verify.VerifyResponseDTO;
import com.foodibd.backend.entity.Addon;
import com.foodibd.backend.entity.MenuItem;
import com.foodibd.backend.repository.AddonRepository;
import com.foodibd.backend.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private final MenuItemRepository menuItemRepository;
    private final AddonRepository addonRepository;

    // Hardcoded promo codes matching mockData.js
    private static final Map<String, PromoEntry> PROMOS = Map.of(
            "FOOD20",    new PromoEntry("percentage", 20),
            "WELCOME50", new PromoEntry("flat", 50)
    );

    private record PromoEntry(String type, int value) {}

    // ── POST /api/cart/promo ──────────────────────────────────────────────────

    public PromoResponseDTO validatePromo(PromoRequestDTO req) {
        String code = req.getPromo_code() == null ? "" : req.getPromo_code().toUpperCase().trim();
        int subtotal    = req.getSubtotal()     == null ? 0 : req.getSubtotal();
        int deliveryFee = req.getDelivery_fee() == null ? 50 : req.getDelivery_fee();

        PromoEntry entry = PROMOS.get(code);

        if (entry == null) {
            return PromoResponseDTO.builder()
                    .promo_code(code)
                    .is_valid(false)
                    .message("Invalid promo code")
                    .sub_total(subtotal)
                    .delivery_fee(deliveryFee)
                    .total(subtotal + deliveryFee)
                    .discount_amount(0)
                    .build();
        }

        int discountAmount = entry.type().equals("percentage")
                ? (subtotal * entry.value() / 100)
                : entry.value();

        int total = subtotal + deliveryFee - discountAmount;

        return PromoResponseDTO.builder()
                .promo_code(code)
                .is_valid(true)
                .discount_type(entry.type())
                .discount_value(entry.value())
                .discount_amount(discountAmount)
                .sub_total(subtotal)
                .delivery_fee(deliveryFee)
                .total(Math.max(total, 0))
                .message("Promo applied successfully")
                .build();
    }

    // ── POST /api/cart/bill ───────────────────────────────────────────────────

    public BillResponseDTO generateBill(BillRequestDTO req) {
        int subtotal = computeSubtotal(req.getItems());
        int deliveryFee = 50;

        String code = req.getPromo_code() == null ? "" : req.getPromo_code().toUpperCase().trim();
        PromoEntry entry = PROMOS.get(code);

        int discount = 0;
        boolean promoApplied = false;
        String message = "Bill generated successfully";

        if (entry != null) {
            discount = entry.type().equals("percentage")
                    ? (subtotal * entry.value() / 100)
                    : entry.value();
            promoApplied = true;
            message = "Promo " + code + " applied";
        }

        int total = Math.max(subtotal + deliveryFee - discount, 0);

        return BillResponseDTO.builder()
                .restaurant_id(req.getRestaurant_id())
                .subtotal(subtotal)
                .discount(discount)
                .delivery_fee(deliveryFee)
                .total(total)
                .promo_applied(promoApplied)
                .message(message)
                .build();
    }

    // ── POST /api/cart/verify ─────────────────────────────────────────────────

    public VerifyResponseDTO verify(VerifyRequestDTO req) {
        if (req.getItems() == null || req.getItems().isEmpty()) {
            return VerifyResponseDTO.builder()
                    .message("Cart is empty")
                    .build();
        }

        for (CartItemDTO cartItem : req.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(cartItem.getItem_id())
                    .orElse(null);

            if (menuItem == null) {
                return VerifyResponseDTO.builder()
                        .message("Item not found: " + cartItem.getItem_id())
                        .build();
            }

            if (Boolean.FALSE.equals(menuItem.getAvailability())) {
                return VerifyResponseDTO.builder()
                        .message(menuItem.getName() + " is currently unavailable")
                        .build();
            }

            // Verify addon IDs exist
            if (cartItem.getAddon_ids() != null) {
                for (Integer addonId : cartItem.getAddon_ids()) {
                    Addon addon = addonRepository.findById(addonId).orElse(null);
                    if (addon == null) {
                        return VerifyResponseDTO.builder()
                                .message("Addon not found: " + addonId)
                                .build();
                    }
                }
            }
        }

        return VerifyResponseDTO.builder()
                .message("Cart verified successfully")
                .build();
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private int computeSubtotal(List<CartItemDTO> items) {
        if (items == null) return 0;
        int subtotal = 0;
        for (CartItemDTO cartItem : items) {
            MenuItem menuItem = menuItemRepository.findById(cartItem.getItem_id())
                    .orElse(null);
            if (menuItem == null) continue;

            int itemPrice = menuItem.getPrice().intValue();

            // Add addon prices
            if (cartItem.getAddon_ids() != null) {
                for (Integer addonId : cartItem.getAddon_ids()) {
                    Addon addon = addonRepository.findById(addonId).orElse(null);
                    if (addon != null) itemPrice += addon.getPrice();
                }
            }

            subtotal += itemPrice * cartItem.getQuantity();
        }
        return subtotal;
    }
}
