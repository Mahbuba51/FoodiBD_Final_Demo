package com.foodibd.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Addon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addonId;

    private String name;        // e.g. "BBQ Sauce", "Egg", "Beef Bacon"
    private Integer price;
    private Boolean isPopular;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;
}
