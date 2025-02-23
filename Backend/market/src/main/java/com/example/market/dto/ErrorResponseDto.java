package com.example.market.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponseDto {
    private String message;
    private String timestamp;
}
