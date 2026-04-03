package com.foodibd.backend.dto.cart.checkout;

import com.foodibd.backend.dto.cart.item.CartItemDTO;
import com.foodibd.backend.entity.enums.PaymentOption;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequestDTO {

    private Integer restaurant_id;
    private List<CartItemDTO> items;
    private String promo_code;
    private String delivery_street;
    private String delivery_city;
    private String delivery_state;
    private String delivery_postal_code;
    private String delivery_country;
    private PaymentOption payment_option;
}