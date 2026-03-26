package com.foodibd.backend.dto.cart.item;


import com.foodibd.backend.dto.cart.item.CartItemCustomizationDTO;

import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {

        private Integer item_id;
        private Integer quantity;
        private List<CartItemCustomizationDTO> customizations;
        private List<Integer> addon_ids;
        private String special_instructions;
}
