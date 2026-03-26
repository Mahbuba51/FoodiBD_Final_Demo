package com.foodibd.backend.dto.menu.getRestaurantMenu;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemSummaryDTO {

    private Integer itemId;
    private String name;
    private String description;
    private Integer price;
    private String thumbnailUrl;
    private Boolean isPopular;
    private Boolean isAvailable;
}
