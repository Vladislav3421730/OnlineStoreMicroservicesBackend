package com.example.market.model;

import com.example.market.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    @Size(min = 3, message = "Username length must be more or equal than 5")
    @NotBlank
    private String username;
    @Size(min = 6, message = "Password length must be more or equal than 6 ")
    @NotBlank
    private String password;

    @Column(unique = true)
    @Email(message = "Email must contains @")
    private String email;
    @Column(name = "is_bun")
    private boolean bun;

    @Column(name = "phone_number", unique = true)
    @Pattern(regexp = "^[+]375[0-9]{9}$", message = "Phone number must be in format +375XXXXXXXXX")
    private String phoneNumber;

    @PrePersist
    private void init() {
        bun = false;
    }

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roleSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @OrderBy("createdAt DESC")
    private List<Order> orders = new ArrayList<>();

    public void addCartToList(Cart cart) {
        carts.add(cart);
        cart.setUser(this);
    }

    public void addOrderToList(Order order) {
        orders.add(order);
        order.setUser(this);
    }
}
