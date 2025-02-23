package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for address")
public class AddressDto {

    @Schema(description = "Address identifier", example = "1")
    private Long id;

    @Schema(description = "Region (for example, state or country)", example = "Moscow")
    private String region;

    @Schema(description = "City or locality", example = "Moscow")
    private String town;

    @Schema(description = "Exact address, such as street and house number", example = "Lenina Street, 5")
    private String exactAddress;

    @Schema(description = "Postal code", example = "101000")
    private String postalCode;
}


