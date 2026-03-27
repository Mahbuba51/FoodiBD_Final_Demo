package com.foodibd.backend.service;

import com.foodibd.backend.dto.cart.bill.BillRequestDTO;
import com.foodibd.backend.dto.cart.bill.BillResponseDTO;
import com.foodibd.backend.dto.cart.checkout.CheckoutRequestDTO;
import com.foodibd.backend.dto.cart.item.CartItemDTO;
import com.foodibd.backend.dto.cart.promo.PromoRequestDTO;
import com.foodibd.backend.dto.cart.promo.PromoResponseDTO;
import com.foodibd.backend.dto.cart.verify.VerifyRequestDTO;
import com.foodibd.backend.dto.cart.verify.VerifyResponseDTO;
import com.foodibd.backend.entity.*;
import com.foodibd.backend.entity.embedded.Address;
import com.foodibd.backend.entity.enums.OrderStatus;
import com.foodibd.backend.entity.enums.PaymentStatus;
import com.foodibd.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private static final int DELIVERY_FEE = 50;

    // ─── VERIFY ──────────────────────────────────────────────────────────────

    public VerifyResponseDTO verify(VerifyRequestDTO request) {
        // Reject empty cart
        if (request.getItems() == null || request.getItems().isEmpty()) {
            return VerifyResponseDTO.builder()
                    .message("Cart is empty.")
                    .build();
        }

        // Check restaurant exists
        if (!restaurantRepository.existsById(request.getRestaurant_id())) {
            return VerifyResponseDTO.builder()
                    .message("Restaurant not found.")
                    .build();
        }

        // Check every item exists and is available
        for (CartItemDTO cartItem : request.getItems()) {
            if (cartItem.getQuantity() == null || cartItem.getQuantity() <= 0) {
                return VerifyResponseDTO.builder()
                        .message("Invalid quantity for item ID " + cartItem.getItem_id())
                        .build();
            }
            Optional<MenuItem> itemOpt = menuItemRepository.findById(cartItem.getItem_id());
            if (itemOpt.isEmpty()) {
                return VerifyResponseDTO.builder()
                        .message("Item with ID " + cartItem.getItem_id() + " not found.")
                        .build();
            }
            if (!itemOpt.get().getAvailability()) {
                return VerifyResponseDTO.builder()
                        .message("Item '" + itemOpt.get().getName() + "' is currently unavailable.")
                        .build();
            }
        }

        return VerifyResponseDTO.builder()
                .message("Cart verified successfully.")
                .build();
    }

    // ─── BILL ────────────────────────────────────────────────────────────────

    public BillResponseDTO generateBill(BillRequestDTO request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            return BillResponseDTO.builder()
                    .message("Cart is empty.")
                    .subtotal(0)
                    .discount(0)
                    .delivery_fee(DELIVERY_FEE)
                    .total(DELIVERY_FEE)
                    .promo_applied(false)
                    .build();
        }

        int subtotal = calculateSubtotal(request.getItems());
        int discount = 0;
        boolean promoApplied = false;
        String message = "Bill generated successfully.";

        if (request.getPromo_code() != null && !request.getPromo_code().isBlank()) {
            PromoResult promo = applyPromo(request.getPromo_code(), subtotal);
            discount = promo.discountAmount();
            promoApplied = promo.applied();
            if (promoApplied) {
                message = "Promo '" + request.getPromo_code() + "' applied successfully.";
            } else {
                message = "Invalid promo code. Full price charged.";
            }
        }

        int total = subtotal - discount + DELIVERY_FEE;

        return BillResponseDTO.builder()
                .restaurant_id(request.getRestaurant_id())
                .subtotal(subtotal)
                .discount(discount)
                .delivery_fee(DELIVERY_FEE)
                .total(total)
                .promo_applied(promoApplied)
                .message(message)
                .build();
    }

    // ─── PROMO ───────────────────────────────────────────────────────────────

    public PromoResponseDTO validatePromo(PromoRequestDTO request) {
        if (request.getPromo_code() == null || request.getPromo_code().isBlank()) {
            return PromoResponseDTO.builder()
                    .is_valid(false)
                    .message("No promo code provided.")
                    .build();
        }

        int subtotal = request.getSubtotal() != null ? request.getSubtotal() : 0;
        PromoResult result = applyPromo(request.getPromo_code(), subtotal);

        if (!result.applied()) {
            return PromoResponseDTO.builder()
                    .promo_code(request.getPromo_code())
                    .is_valid(false)
                    .message("Invalid or expired promo code.")
                    .build();
        }

        int total = subtotal - result.discountAmount() + DELIVERY_FEE;

        return PromoResponseDTO.builder()
                .promo_code(request.getPromo_code())
                .is_valid(true)
                .discount_type(result.discountType())
                .discount_value(result.discountValue())
                .discount_amount(result.discountAmount())
                .sub_total(subtotal)
                .delivery_fee(DELIVERY_FEE)
                .total(total)
                .message("Promo code applied successfully.")
                .build();
    }

    // ─── CHECKOUT ────────────────────────────────────────────────────────────

    @Transactional
    public Order checkout(CheckoutRequestDTO request) {
        // Validate cart is not empty
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot checkout with an empty cart.");
        }

        // Validate restaurant exists
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurant_id())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Restaurant not found: " + request.getRestaurant_id()));

        // Build delivery address from flat DTO fields
        Address deliveryAddress = Address.builder()
                .street(request.getDelivery_street())
                .city(request.getDelivery_city())
                .state(request.getDelivery_state())
                .postalCode(request.getDelivery_postal_code())
                .country(request.getDelivery_country())
                .build();

        // Calculate totals
        int subtotal = calculateSubtotal(request.getItems());
        int discount = 0;
        if (request.getPromo_code() != null && !request.getPromo_code().isBlank()) {
            PromoResult promo = applyPromo(request.getPromo_code(), subtotal);
            if (promo.applied()) discount = promo.discountAmount();
        }
        int total = subtotal - discount + DELIVERY_FEE;

        // Build and save the Order
        Order order = Order.builder()
                .restaurant(restaurant)
                .deliveryAddress(deliveryAddress)
                .status(OrderStatus.CREATED)
                .paymentStatus(false)           // false = not yet paid (Boolean field on entity)
                .paymentOption(request.getPayment_option())
                .createdAt(LocalDateTime.now())
                .estimatedDeliveryTime(LocalDateTime.now().plusMinutes(45))
                .build();

        order = orderRepository.save(order);

        // Build and save OrderItems
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemDTO cartItem : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(cartItem.getItem_id())
                    .orElseThrow(() -> new IllegalArgumentException(
                        "Menu item not found: " + cartItem.getItem_id()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .menuItem(menuItem)
                    .quantity(cartItem.getQuantity())
                    .priceAtPurchase(menuItem.getPrice())
                    .build();

            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);
        return order;
    }

    // ─── PRIVATE HELPERS ─────────────────────────────────────────────────────

    private int calculateSubtotal(List<CartItemDTO> items) {
        int subtotal = 0;
        for (CartItemDTO cartItem : items) {
            Optional<MenuItem> itemOpt = menuItemRepository.findById(cartItem.getItem_id());
            if (itemOpt.isPresent()) {
                double price = itemOpt.get().getPrice();
                subtotal += (int) Math.round(price * cartItem.getQuantity());
            }
        }
        return subtotal;
    }

    private PromoResult applyPromo(String promoCode, int subtotal) {
        return switch (promoCode.toUpperCase()) {
            case "FOODI10" -> new PromoResult(true, "percentage", 10,  subtotal / 10);
            case "FOODI50" -> new PromoResult(true, "flat",       50,  50);
            case "WELCOME" -> new PromoResult(true, "percentage", 15,  (int)(subtotal * 0.15));
            default        -> new PromoResult(false, null,         0,   0);
        };
    }

    private record PromoResult(
        boolean applied,
        String discountType,
        int discountValue,
        int discountAmount
    ) {}
}