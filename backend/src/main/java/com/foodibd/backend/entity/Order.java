package com.foodibd.backend.entity;

import com.foodibd.backend.entity.embedded.Address;
import com.foodibd.backend.entity.enums.OrderStatus;
import com.foodibd.backend.entity.enums.PaymentOption;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderID;

    private Integer cartID;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Boolean paymentStatus;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street",     column = @Column(name = "delivery_street")),
        @AttributeOverride(name = "city",       column = @Column(name = "delivery_city")),
        @AttributeOverride(name = "state",      column = @Column(name = "delivery_state")),
        @AttributeOverride(name = "postalCode", column = @Column(name = "delivery_postal_code")),
        @AttributeOverride(name = "country",    column = @Column(name = "delivery_country"))
    })
    private Address deliveryAddress;

    private LocalDateTime createdAt;
    private LocalDateTime estimatedDeliveryTime;

    @Enumerated(EnumType.STRING)
    private PaymentOption paymentOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
