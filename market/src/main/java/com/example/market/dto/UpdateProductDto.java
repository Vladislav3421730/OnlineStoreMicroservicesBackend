package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for updating product details")
public class UpdateProductDto {

    @NotNull(message = "id must be not null")
    @Schema(description = "Unique identifier for the product", example = "1")
    private Long id;

    @Schema(description = "Title of the product", example = "Smartphone", required = true)
    @NotBlank(message = "Title is required")
    @Size(min = 3, message = "Title's size must be more or equal than 3")
    private String title;

    @Schema(description = "Description of the product", example = "Latest smartphone with 6GB RAM", required = true)
    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description's size must be more or equal than 10")
    private String description;

    @Schema(description = "Category of the product", example = "Electronics", required = true)
    @NotBlank(message = "Category is required")
    @Size(min = 3, message = "Category's size must be more or equal than 3")
    private String category;

    @Schema(description = "Amount of the product in stock", example = "50", required = true)
    @Min(value = 0, message = "Amount must be more or equal than 0")
    @NotNull(message = "Amount is required")
    private Integer amount;

    @Schema(description = "Cost of the product", example = "599.99", required = true)
    @DecimalMin(value = "0.01", message = "Cost must be greater than or equal to 0.01")
    @NotNull(message = "Coast is required")
    private BigDecimal coast;
}
