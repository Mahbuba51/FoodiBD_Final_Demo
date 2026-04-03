package com.foodibd.backend.repository;

import com.foodibd.backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    List<Restaurant> findByNameContainingIgnoreCase(String name);
    List<Restaurant> findByAvailableTrue();
}
