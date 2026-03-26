package com.foodibd.backend.dto.menu.getMenuItemDetails;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomizationDTO {

    private Integer customizationId;
    private String description;
    private List<CustomizationOptionDTO> options;
}
