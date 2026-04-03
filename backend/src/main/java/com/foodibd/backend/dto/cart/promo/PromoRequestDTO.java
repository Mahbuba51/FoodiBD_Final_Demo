package com.foodibd.backend.dto.cart.promo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoRequestDTO {

    private String promo_code;
    private Integer restaurant_id;
    private Integer subtotal;
    private Integer delivery_fee;
}
