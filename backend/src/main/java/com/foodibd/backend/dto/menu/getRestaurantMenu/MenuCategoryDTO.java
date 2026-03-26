package com.foodibd.backend.dto.menu.getRestaurantMenu;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuCategoryDTO {

    private Integer categoryId;
    private String categoryName;
    private List<MenuItemSummaryDTO> items;
}
