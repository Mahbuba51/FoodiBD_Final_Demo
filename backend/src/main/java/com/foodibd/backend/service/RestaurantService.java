package com.foodibd.backend.service;

import com.foodibd.backend.dto.menu.getMenuItemDetails.*;
import com.foodibd.backend.dto.menu.getRestaurantMenu.*;
import com.foodibd.backend.dto.restaurant.getRestaurantDetails.RestaurantDetailsResponseDTO;
import com.foodibd.backend.entity.MenuItem;
import com.foodibd.backend.entity.Restaurant;
import com.foodibd.backend.repository.MenuItemRepository;
import com.foodibd.backend.repository.MenuRepository;
import com.foodibd.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;

    // ── GET /restaurants/{id} ─────────────────────────────────────────────────

    public RestaurantDetailsResponseDTO getRestaurantDetails(Integer restaurantId) {
        Restaurant r = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found: " + restaurantId));

        return RestaurantDetailsResponseDTO.builder()
                .restaurantId(r.getRestaurantID())
                .name(r.getName())
                .cuisineTags(r.getCuisineTypes())
                .rating(r.getRating())
                .ratingCount(557)
                .distanceM(509)
                .deliveryTimeMin(25)
                .deliveryTimeMax(40)
                .deliveryFee(50)
                .deliveredBy("Delivered by Foodi")
                .bannerUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&h=300&fit=crop")
                .logoUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=100&h=100&fit=crop")
                .isOpen(r.getAvailable())
                .openingTime("10:00")
                .closingTime("23:00")
                .build();
    }

    // ── GET /restaurants/{id}/menu ────────────────────────────────────────────

    public MenuResponseDTO getRestaurantMenu(Integer restaurantId, String query) {
        var menu = menuRepository.findByRestaurantRestaurantID(restaurantId)
                .orElseThrow(() -> new RuntimeException("Menu not found for restaurant: " + restaurantId));

        List<MenuItem> items = menuItemRepository.findByMenuMenuID(menu.getMenuID());

        // Optional search filter
        if (query != null && !query.isBlank()) {
            String q = query.toLowerCase();
            items = items.stream()
                    .filter(i -> i.getName().toLowerCase().contains(q)
                            || i.getDescription().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        // Group items by their first category tag
        Map<String, List<MenuItem>> grouped = items.stream()
                .collect(Collectors.groupingBy(
                        i -> (i.getCategories() != null && !i.getCategories().isEmpty())
                                ? i.getCategories().get(0)
                                : "Other"
                ));

        List<MenuCategoryDTO> categories = grouped.entrySet().stream()
                .map(entry -> MenuCategoryDTO.builder()
                        .categoryName(entry.getKey())
                        .items(entry.getValue().stream()
                                .map(this::toSummaryDTO)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return MenuResponseDTO.builder()
                .restaurantId(restaurantId)
                .categories(categories)
                .build();
    }

    // ── GET /restaurants/{id}/menu/items/{itemId} ─────────────────────────────

    public MenuItemDetailsResponseDTO getMenuItemDetails(Integer restaurantId, Integer itemId) {
        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("MenuItem not found: " + itemId));

        List<CustomizationDTO> customizationDTOs = item.getCustomizations() == null
                ? List.of()
                : item.getCustomizations().stream()
                        .map(c -> CustomizationDTO.builder()
                                .customizationId(c.getCustomizationId())
                                .description(c.getDescription())
                                .options(c.getOptions() == null ? List.of() :
                                        c.getOptions().stream()
                                                .map(o -> CustomizationOptionDTO.builder()
                                                        .optionId(o.getOptionId())
                                                        .optionName(o.getOptionName())
                                                        .extraPrice(o.getExtraPrice())
                                                        .isDefault(o.getIsDefault())
                                                        .build())
                                                .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList());

        List<AddonDTO> addonDTOs = item.getAddons() == null
                ? List.of()
                : item.getAddons().stream()
                        .map(a -> AddonDTO.builder()
                                .addonId(a.getAddonId())
                                .name(a.getName())
                                .price(a.getPrice())
                                .isPopular(a.getIsPopular())
                                .build())
                        .collect(Collectors.toList());

        return MenuItemDetailsResponseDTO.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .description(item.getDescription())
                .basePrice(item.getPrice().intValue())
                .thumbnailUrl(item.getThumbnailUrl())
                .isPopular(item.getIsPopular())
                .isAvailable(item.getAvailability())
                .customizations(customizationDTOs)
                .addons(addonDTOs)
                .build();
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private MenuItemSummaryDTO toSummaryDTO(MenuItem item) {
        return MenuItemSummaryDTO.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice().intValue())
                .thumbnailUrl(item.getThumbnailUrl())
                .isPopular(item.getIsPopular())
                .isAvailable(item.getAvailability())
                .build();
    }
}
