package com.foodibd.backend.dto.cart.item;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemCustomizationDTO {

    private Integer customization_id;
    private Integer option_id;
}
