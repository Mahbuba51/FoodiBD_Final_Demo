package com.foodibd.backend.dto.cart.bill;

import lombok.*;
import java.util.List;
import com.foodibd.backend.dto.cart.item.CartItemDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillRequestDTO {

    private Integer restaurant_id;
    private List<CartItemDTO> items;
    private String promo_code;

}