package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing an image associated with a product")
public class ImageDto {

    @Schema(description = "Unique identifier of the image", example = "1")
    private Long id;

    @NotBlank(message = "File name should be not empty and blank")
    @Schema(description = "Name of the image file", example = "image.jpg")
    private String fileName;

    @NotBlank(message = "Type should be not empty and blank")
    @Schema(description = "Type of the image (e.g., 'image/jpeg', 'image/png')", example = "image/jpeg")
    private String type;

    @NotBlank(message = "File path should be not empty and blank")
    @Schema(description = "Path where the image file is stored", example = "/images/products/image.jpg")
    private String filePath;

    @Schema(description = "ID of the associated product", example = "101")
    private Long productId;
}


