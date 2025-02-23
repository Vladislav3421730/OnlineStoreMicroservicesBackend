package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO for order item details")
public class OrderItemDto {

    @Schema(description = "Unique identifier for the order item", example = "1")
    private Long id;

    @Schema(description = "Quantity of the product in the order item", example = "2")
    private int amount;

    @Schema(description = "Product details associated with the order item")
    private ProductDto product;

    @Schema(description = "ID of the order to which the item belongs", example = "101")
    private Long orderId;
}