package com.foodibd.backend.controller;

import com.foodibd.backend.dto.restaurant.getRestaurantDetails.RestaurantDetailsResponseDTO;
import com.foodibd.backend.dto.menu.getRestaurantMenu.MenuResponseDTO;
import com.foodibd.backend.dto.menu.getMenuItemDetails.MenuItemDetailsResponseDTO;
import com.foodibd.backend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/{restaurant_id}")
    public ResponseEntity<RestaurantDetailsResponseDTO> getRestaurantDetails(
            @PathVariable("restaurant_id") Integer restaurantId) {

        return ResponseEntity.ok(restaurantService.getRestaurantDetails(restaurantId));
    }

    @GetMapping("/{restaurant_id}/menu")
    public ResponseEntity<MenuResponseDTO> getRestaurantMenu(
            @PathVariable("restaurant_id") Integer restaurantId,
            @RequestParam(value = "query", required = false) String query) {

        return ResponseEntity.ok(restaurantService.getRestaurantMenu(restaurantId, query));
    }

    @GetMapping("/{restaurant_id}/menu/items/{item_id}")
    public ResponseEntity<MenuItemDetailsResponseDTO> getMenuItemDetails(
            @PathVariable("restaurant_id") Integer restaurantId,
            @PathVariable("item_id") Integer itemId) {

        return ResponseEntity.ok(restaurantService.getMenuItemDetails(restaurantId, itemId));
    }
}
