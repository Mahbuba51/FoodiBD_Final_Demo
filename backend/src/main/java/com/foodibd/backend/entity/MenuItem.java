package com.foodibd.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "menu_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    private String name;
    private String description;
    private Double price;
    private Boolean availability;
    private Boolean isPopular;
    private String thumbnailUrl;

    @ElementCollection
    @CollectionTable(name = "menu_item_categories", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "category")
    private List<String> categories;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Customization> customizations;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Addon> addons;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
}
