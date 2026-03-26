package com.foodibd.backend.dto.cart.bill;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResponseDTO {

    private Integer restaurant_id;

    private Integer subtotal;
    private Integer discount;
    private Integer delivery_fee;
    private Integer total;

    private Boolean promo_applied;
    private String message;

}