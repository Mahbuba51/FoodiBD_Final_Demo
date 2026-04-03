package com.foodibd.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "customizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customizationId;

    // e.g. "Choose Your Bun", "Spice Level", "Size"
    private String description;

    @OneToMany(mappedBy = "customization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CustomizationOption> options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;
}
