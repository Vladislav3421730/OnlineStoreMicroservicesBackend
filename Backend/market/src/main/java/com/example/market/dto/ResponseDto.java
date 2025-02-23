package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO for standard response message")
public class ResponseDto {

    @Schema(description = "Message in the response", example = "Operation completed successfully")
    private String message;
}
