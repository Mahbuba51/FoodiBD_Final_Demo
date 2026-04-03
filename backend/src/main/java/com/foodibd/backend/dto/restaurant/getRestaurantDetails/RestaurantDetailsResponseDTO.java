package com.foodibd.backend.dto.restaurant.getRestaurantDetails;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDetailsResponseDTO {

    private Integer restaurantId;
    private String name;
    private List<String> cuisineTags;
    private Double rating;
    private Integer ratingCount;
    private Integer distanceM;
    private Integer deliveryTimeMin;
    private Integer deliveryTimeMax;
    private Integer deliveryFee;
    private String deliveredBy;
    private String bannerUrl;
    private String logoUrl;
    private Boolean isOpen;
    private String openingTime;
    private String closingTime;
}
