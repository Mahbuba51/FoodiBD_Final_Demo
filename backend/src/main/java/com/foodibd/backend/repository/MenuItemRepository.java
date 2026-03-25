package com.foodibd.backend.repository;

import com.foodibd.backend.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    List<MenuItem> findByMenuMenuID(Integer menuId);
    List<MenuItem> findByAvailabilityTrue();
}
