package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing an image related to a product")
public class CreateImageDto {

    @Schema(description = "Name of the image file", example = "image.jpg")
    private String fileName;

    @Schema(description = "Type of the image (e.g., 'image/jpeg', 'image/png')", example = "image/jpeg")
    private String type;

    @Schema(description = "File path where the image is stored", example = "/images/products/image.jpg")
    private String filePath;

    @Schema(description = "Byte array containing image data")
    private byte[] fileData;
}

