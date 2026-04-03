package com.foodibd.backend.dto.menu.getMenuItemDetails;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemDetailsResponseDTO {

    private Integer itemId;
    private String name;
    private String description;
    private Integer basePrice;
    private String thumbnailUrl;
    private Boolean isPopular;
    private Boolean isAvailable;
    private List<CustomizationDTO> customizations;
    private List<AddonDTO> addons;
}
