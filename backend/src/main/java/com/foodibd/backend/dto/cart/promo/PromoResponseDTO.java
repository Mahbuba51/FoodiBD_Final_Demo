package com.foodibd.backend.dto.cart.promo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoResponseDTO {

    private String promo_code;
    private Boolean is_valid;
    private String discount_type;   // "flat" or "percentage"
    private Integer discount_value;
    private Integer discount_amount;
    private Integer sub_total;
    private Integer total;
    private Integer delivery_fee;
    private String message;
}