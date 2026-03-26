package com.foodibd.backend.controller;

import com.foodibd.backend.dto.restaurant.getRestaurantDetails.RestaurantDetailsResponseDTO;
import com.foodibd.backend.dto.menu.getRestaurantMenu.MenuResponseDTO;
import com.foodibd.backend.dto.menu.getMenuItemDetails.MenuItemDetailsResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @GetMapping("/{restaurant_id}")
    public ResponseEntity<RestaurantDetailsResponseDTO> getRestaurantDetails(
            @PathVariable("restaurant_id") Integer restaurantId) {

        // TODO: call service layer
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{restaurant_id}/menu")
    public ResponseEntity<MenuResponseDTO> getRestaurantMenu(
            @PathVariable("restaurant_id") Integer restaurantId,
            @RequestParam(value = "query", required = false) String query) {

        // TODO: call service layer
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{restaurant_id}/menu/items/{item_id}")
    public ResponseEntity<MenuItemDetailsResponseDTO> getMenuItemDetails(
            @PathVariable("restaurant_id") Integer restaurantId,
            @PathVariable("item_id") Integer itemId) {

        // TODO: call service layer
        return ResponseEntity.ok().build();
    }
}