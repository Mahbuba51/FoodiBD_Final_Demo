package com.foodibd.backend.controller;

import com.foodibd.backend.dto.restaurant.getRestaurantDetails.RestaurantDetailsResponseDTO;
import com.foodibd.backend.dto.menu.getRestaurantMenu.MenuResponseDTO;
import com.foodibd.backend.dto.menu.getMenuItemDetails.MenuItemDetailsResponseDTO;
import com.foodibd.backend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // ─── GET RESTAURANT DETAILS ──────────────────────────────────────────────

    @GetMapping("/{restaurant_id}")
    public ResponseEntity<RestaurantDetailsResponseDTO> getRestaurantDetails(
            @PathVariable("restaurant_id") Integer restaurantId) {

        RestaurantDetailsResponseDTO response = restaurantService.getRestaurantDetails(restaurantId);
        return ResponseEntity.ok(response);
    }

    // ─── GET RESTAURANT MENU ─────────────────────────────────────────────────

    @GetMapping("/{restaurant_id}/menu")
    public ResponseEntity<MenuResponseDTO> getRestaurantMenu(
            @PathVariable("restaurant_id") Integer restaurantId,
            @RequestParam(value = "query", required = false) String query) {

        MenuResponseDTO response = restaurantService.getRestaurantMenu(restaurantId, query);
        return ResponseEntity.ok(response);
    }

    // ─── GET MENU ITEM DETAILS ───────────────────────────────────────────────

    @GetMapping("/{restaurant_id}/menu/items/{item_id}")
    public ResponseEntity<MenuItemDetailsResponseDTO> getMenuItemDetails(
            @PathVariable("restaurant_id") Integer restaurantId,
            @PathVariable("item_id") Integer itemId) {

        MenuItemDetailsResponseDTO response = restaurantService.getMenuItemDetails(restaurantId, itemId);
        return ResponseEntity.ok(response);
    }

    // ─── EXCEPTION HANDLERS ──────────────────────────────────────────────────
    
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage() != null ? ex.getMessage() : "Resource not found."));
    }
}