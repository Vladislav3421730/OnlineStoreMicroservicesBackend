package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO for JWT response containing access and refresh tokens")
public class JwtResponseDto {

    @Schema(description = "Access token used for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiZW1haWwiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjI4MjM2NzQ1fQ.k3rUfj1DikFbd1k6B3hJHfD9_xD6wM8uP9YzJ8gPrFs")
    private String accessToken;

    @Schema(description = "Refresh token used to generate a new access token", example = "c09af6e6-22ec-43d5-836b-5d5e6a47b22f")
    private String refreshToken;
}