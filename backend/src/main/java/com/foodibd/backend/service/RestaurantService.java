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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;

    // ─── GET RESTAURANT DETAILS ──────────────────────────────────────────────

    public RestaurantDetailsResponseDTO getRestaurantDetails(Integer restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Restaurant not found: " + restaurantId));

        return RestaurantDetailsResponseDTO.builder()
                .restaurantId(restaurant.getRestaurantID())
                .name(restaurant.getName())
                .cuisineTags(restaurant.getCuisineTypes())
                .rating(restaurant.getRating())
                // Fields not in entity — safe demo defaults below
                .ratingCount(120)
                .distanceM(1200)
                .deliveryTimeMin(20)
                .deliveryTimeMax(35)
                .deliveryFee(50)
                .deliveredBy("Foodi Express")
                .bannerUrl(null)
                .logoUrl(null)
                .isOpen(restaurant.getAvailable())
                .openingTime("10:00")
                .closingTime("23:00")
                .build();
    }

    // ─── GET RESTAURANT MENU ─────────────────────────────────────────────────

    public MenuResponseDTO getRestaurantMenu(Integer restaurantId, String query) {
        // Verify restaurant exists
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Restaurant not found: " + restaurantId));

        // Get the menu for this restaurant
        var menu = menuRepository.findByRestaurantRestaurantID(restaurantId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Menu not found for restaurant: " + restaurantId));

        // Get all items, optionally filtered by search query
        List<MenuItem> allItems = menuItemRepository.findByMenuMenuID(menu.getMenuID());

        if (query != null && !query.isBlank()) {
            String lowerQuery = query.toLowerCase();
            allItems = allItems.stream()
                    .filter(item -> item.getName().toLowerCase().contains(lowerQuery)
                            || (item.getDescription() != null
                                && item.getDescription().toLowerCase().contains(lowerQuery)))
                    .collect(Collectors.toList());
        }

        // Group items by their first category tag
        // e.g. items tagged ["Burgers", "Bestseller"] go into the "Burgers" category
        Map<String, List<MenuItem>> grouped = new LinkedHashMap<>();
        for (MenuItem item : allItems) {
            String category = (item.getCategories() != null && !item.getCategories().isEmpty())
                    ? item.getCategories().get(0)
                    : "Other";
            grouped.computeIfAbsent(category, k -> new ArrayList<>()).add(item);
        }

        // Build category DTOs
        List<MenuCategoryDTO> categories = new ArrayList<>();
        int categoryIdCounter = 1;
        for (Map.Entry<String, List<MenuItem>> entry : grouped.entrySet()) {
            List<MenuItemSummaryDTO> summaries = entry.getValue().stream()
                    .map(item -> MenuItemSummaryDTO.builder()
                            .itemId(item.getItemId())
                            .name(item.getName())
                            .description(item.getDescription())
                            .price(item.getPrice() != null
                                    ? (int) Math.round(item.getPrice()) : 0)
                            .thumbnailUrl(null)
                            .isPopular(false)
                            .isAvailable(item.getAvailability())
                            .build())
                    .collect(Collectors.toList());

            categories.add(MenuCategoryDTO.builder()
                    .categoryId(categoryIdCounter++)
                    .categoryName(entry.getKey())
                    .items(summaries)
                    .build());
        }

        return MenuResponseDTO.builder()
                .restaurantId(restaurantId)
                .categories(categories)
                .build();
    }

    // ─── GET MENU ITEM DETAILS ───────────────────────────────────────────────

    public MenuItemDetailsResponseDTO getMenuItemDetails(Integer restaurantId, Integer itemId) {
        // Verify restaurant exists
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Restaurant not found: " + restaurantId));

        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Menu item not found: " + itemId));

        return MenuItemDetailsResponseDTO.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .description(item.getDescription())
                .basePrice(item.getPrice() != null
                        ? (int) Math.round(item.getPrice()) : 0)
                .thumbnailUrl(null)
                .isPopular(false)
                .isAvailable(item.getAvailability())
                // No customizations or addons in DB yet — empty lists are safe for demo
                .customizations(Collections.emptyList())
                .addons(Collections.emptyList())
                .build();
    }
}