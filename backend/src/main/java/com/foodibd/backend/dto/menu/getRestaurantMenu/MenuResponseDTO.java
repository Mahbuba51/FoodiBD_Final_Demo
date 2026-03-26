package com.foodibd.backend.dto.menu.getRestaurantMenu;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponseDTO {

    private Integer restaurantId;
    private List<MenuCategoryDTO> categories;
}
