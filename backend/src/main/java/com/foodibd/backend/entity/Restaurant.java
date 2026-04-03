package com.foodibd.backend.entity;

import com.foodibd.backend.entity.embedded.Address;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restaurantID;

    private String name;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street",     column = @Column(name = "address_street")),
        @AttributeOverride(name = "city",       column = @Column(name = "address_city")),
        @AttributeOverride(name = "state",      column = @Column(name = "address_state")),
        @AttributeOverride(name = "postalCode", column = @Column(name = "address_postal_code")),
        @AttributeOverride(name = "country",    column = @Column(name = "address_country"))
    })
    private Address address;

    @ElementCollection
    @CollectionTable(name = "restaurant_cuisine_types", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "cuisine_type")
    private List<String> cuisineTypes;

    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Menu menu;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    private Double rating;
    private Boolean available;
}
