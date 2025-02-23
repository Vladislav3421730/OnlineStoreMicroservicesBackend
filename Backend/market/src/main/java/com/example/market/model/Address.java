package com.example.market.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_id_seq")
    @SequenceGenerator(name = "address_id_seq", sequenceName = "address_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "region must be not empty and blank")
    private String region;

    @NotBlank(message = "town must be not empty and blank")
    private String town;

    @NotBlank(message = "exact address must be not empty anf blank")
    @Column(name = "exact_address")
    private String exactAddress;

    @Column(name = "postal_code")
    private String postalCode;

}
