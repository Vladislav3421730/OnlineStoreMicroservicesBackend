package com.example.factory;

import com.example.market.dto.CreateProductDto;
import com.example.market.dto.ProductDto;
import com.example.market.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ProductFactory {
    public static Product createValidProduct() {
        return Product.builder()
                .title("Valid Title")
                .description("Valid description with enough length")
                .category("Valid Category")
                .amount(10)
                .price(new BigDecimal("10.50"))
                .build();
    }

    public static Product createInvalidProduct() {
        return Product.builder()
                .title("Valid Title")
                .amount(10)
                .price(new BigDecimal("10.50"))
                .build();
    }

    public static ProductDto createProductDto(Long id, String title) {
        return ProductDto.builder()
                .id(id)
                .title(title)
                .build();
    }

    public static Product createProduct(Long id, String title) {
        return Product.builder()
                .id(id)
                .title(title)
                .build();
    }

    public static CreateProductDto createProductDto() {
        return CreateProductDto.builder()
                .title("New Laptop")
                .description("Laptop with 16GB RAM")
                .amount(5)
                .category("Electronics")
                .price(BigDecimal.valueOf(200.3))
                .build();
    }

    public static ProductDto createUpdatedProductDto() {
        return ProductDto.builder()
                .id(1L)
                .title("New Laptop")
                .description("Laptop with 16GB RAM")
                .amount(5)
                .category("Electronics")
                .price(BigDecimal.valueOf(200.3))
                .build();
    }

    public static ProductDto createProductDtoWithZeroAmount() {
        return ProductDto.builder()
                .id(1L)
                .imageList(new ArrayList<>())
                .title("New Laptop")
                .description("Laptop with 16GB RAM")
                .amount(0)
                .category("Electronics")
                .price(BigDecimal.valueOf(200.3))
                .build();
    }

    public static CreateProductDto createInvalidProductDto() {
        return CreateProductDto.builder()
                .amount(5)
                .category("Electronics")
                .price(BigDecimal.valueOf(200.3))
                .build();
    }
}
