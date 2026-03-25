package com.foodibd.backend.repository;

import com.foodibd.backend.entity.Addon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddonRepository extends JpaRepository<Addon, Integer> {
}
