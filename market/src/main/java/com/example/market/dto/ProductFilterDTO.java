package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for filtering products")
public class ProductFilterDTO {

    @Schema(description = "Sort order for the products (e.g., 'asc' or 'desc')", example = "asc")
    private String sort;

    @Schema(description = "Category to filter products by", example = "Electronics")
    private String category;

    @Schema(description = "Minimum price to filter products by", example = "100.00")
    private BigDecimal minPrice;

    @Schema(description = "Maximum price to filter products by", example = "500.00")
    private BigDecimal maxPrice;

    @Schema(description = "Title or part of the title to filter products by", example = "Smartphone")
    private String title;
}
