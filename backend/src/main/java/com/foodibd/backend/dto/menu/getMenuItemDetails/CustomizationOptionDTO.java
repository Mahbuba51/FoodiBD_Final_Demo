package com.foodibd.backend.dto.menu.getMenuItemDetails;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomizationOptionDTO {

    private Integer optionId;
    private String optionName;
    private Integer extraPrice;
    private Boolean isDefault;
}
