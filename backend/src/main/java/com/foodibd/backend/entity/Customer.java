package com.foodibd.backend.entity;

import com.foodibd.backend.entity.embedded.Address;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerID;

    private String name;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street",     column = @Column(name = "address_street")),
        @AttributeOverride(name = "city",       column = @Column(name = "address_city")),
        @AttributeOverride(name = "state",      column = @Column(name = "address_state")),
        @AttributeOverride(name = "postalCode", column = @Column(name = "address_postal_code")),
        @AttributeOverride(name = "country",    column = @Column(name = "address_country"))
    })
    private Address address;
}
