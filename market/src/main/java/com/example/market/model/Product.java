package com.example.market.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq", allocationSize = 1)
    private Long id;

    @Size(min = 3, message = "title's size must more or equal than 3")
    @NotBlank
    private String title;
    @Column(columnDefinition = "TEXT")
    @Size(min = 10, message = "description's size must more or equal than 10")
    @NotBlank
    private String description;
    @Size(min = 3, message = "category's size must more or equal than 3")
    @NotBlank
    private String category;
    @Min(value = 0, message = "amount must be more or equal than 0")
    private Integer amount;

    @DecimalMin(value = "0.01", message = "Cost must be greater than or equal to 10.3")
    private BigDecimal coast;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product", orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Image> imageList = new ArrayList<>();

    public void addImageToList(Image image){
        imageList.add(image);
        image.setProduct(this);
    }

}
