package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Cart DTO representing the user's shopping cart with product details")
public class CartDto {

    @Schema(description = "Unique identifier for the cart", example = "1")
    private Long id;

    @Schema(description = "The quantity of the product in the cart", example = "3")
    private int amount;

    @Schema(description = "Product details added to the cart")
    private ProductDto product;

    @Schema(description = "User ID who owns the cart", example = "101")
    private Long userId;
}

