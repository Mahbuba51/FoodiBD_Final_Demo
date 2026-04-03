package com.foodibd.backend.repository;

import com.foodibd.backend.entity.Order;
import com.foodibd.backend.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCustomerCustomerID(Integer customerId);
    List<Order> findByRestaurantRestaurantID(Integer restaurantId);
    List<Order> findByStatus(OrderStatus status);
}
