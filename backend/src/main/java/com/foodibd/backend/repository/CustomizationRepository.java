package com.foodibd.backend.repository;

import com.foodibd.backend.entity.Customization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomizationRepository extends JpaRepository<Customization, Integer> {
}
