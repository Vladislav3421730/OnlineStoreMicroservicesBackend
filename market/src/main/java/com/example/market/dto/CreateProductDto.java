package com.example.market.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for creating a new product with necessary details")
public class CreateProductDto {

    @Schema(description = "Title of the product", example = "Product A")
    @NotBlank(message = "Title is required")
    @Size(min = 3, message = "Title's size must be more or equal than 3")
    private String title;

    @Schema(description = "Description of the product", example = "A detailed description of the product.")
    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description's size must be more or equal than 10")
    private String description;

    @Schema(description = "Category the product belongs to", example = "Electronics")
    @NotBlank(message = "Category is required")
    @Size(min = 3, message = "Category's size must be more or equal than 3")
    private String category;

    @Schema(description = "Available quantity of the product", example = "100")
    @Min(value = 0, message = "Amount must be more or equal than 0")
    @NotNull(message = "Amount is required")
    private Integer amount;

    @Schema(description = "Cost of the product", example = "19.99")
    @DecimalMin(value = "0.01", message = "Cost must be greater than or equal to 0.01")
    @NotNull(message = "Coast is required")
    private BigDecimal coast;
}
