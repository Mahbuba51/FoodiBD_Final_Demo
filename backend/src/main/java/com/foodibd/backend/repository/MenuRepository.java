package com.foodibd.backend.repository;

import com.foodibd.backend.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Optional<Menu> findByRestaurantRestaurantID(Integer restaurantId);
}
