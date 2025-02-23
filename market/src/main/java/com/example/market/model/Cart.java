package com.example.market.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_of_goods")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_id_seq")
    @SequenceGenerator(name = "cart_id_seq", sequenceName = "cart_id_seq", allocationSize = 1)
    private Long id;

    @Min(value = 1, message = "cart's must must be more or equal than 1")
    private int amount;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @NotNull(message = "The product in the cart cannot be null")
    private Product product;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull(message = "User in cart cannot be null")
    private User user;

    public Cart(Product product) {
        this.amount = 1;
        this.product = product;
    }
}
