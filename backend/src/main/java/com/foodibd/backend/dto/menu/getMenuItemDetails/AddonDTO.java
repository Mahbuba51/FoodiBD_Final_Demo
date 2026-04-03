package com.foodibd.backend.dto.menu.getMenuItemDetails;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddonDTO {

    private Integer addonId;
    private String name;
    private Integer price;
    private Boolean isPopular;
}
