package com.example.market.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_id_seq")
    @SequenceGenerator(name = "image_id_seq", sequenceName = "image_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "File name should be not empty and blank")
    private String fileName;

    @NotBlank(message = "Type should be not empty and blank")
    private String type;

    @Column(name = "path")
    @NotBlank(message = "File path should be not empty and blank")
    private String filePath;

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;
}
