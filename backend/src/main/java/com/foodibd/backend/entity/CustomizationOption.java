package com.foodibd.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customization_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomizationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer optionId;

    private String optionName;   // e.g. "Brioche Bun", "Mild", "Large"
    private Integer extraPrice;  // 0 if no extra cost
    private Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customization_id")
    private Customization customization;
}
