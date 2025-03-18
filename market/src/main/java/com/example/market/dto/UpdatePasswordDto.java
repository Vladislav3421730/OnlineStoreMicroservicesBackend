package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for updating user's password")
public class UpdatePasswordDto {

    @Schema(description = "Unique identifier for the user", example = "1")
    @NotNull(message = "id must be not null")
    @Min(value = 1, message = "id must be greater than or equal to 1")
    private Long id;

    @Schema(description = "old user's password")
    @Size(min = 6, message = "Old password must be at least 6 characters long.")
    @NotBlank(message = "Old password cannot be blank.")
    private String oldPassword;

    @Schema(description = "new user's password")
    @Size(min = 6, message = "New password must be at least 6 characters long.")
    @NotBlank(message = "New password cannot be blank.")
    private String newPassword;

    @Schema(description = "confirm user's new password")
    @Size(min = 6, message = "Confirm password must be at least 6 characters long.")
    @NotBlank(message = "Confirm password cannot be blank.")
    private String confirmNewPassword;
}
