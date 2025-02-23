package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for requesting a new access token using the refresh token")
public class TokenRefreshRequestDto {

    @NotNull(message = "Refresh token should be not null")
    @Schema(description = "The refresh token used to request a new access token",
            example = "c09af6e6-22ec-43d5-836b-5d5e6a47b22f")
    private String refreshToken;
}

