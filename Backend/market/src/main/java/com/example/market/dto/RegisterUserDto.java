package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for registering a new user")
public class RegisterUserDto {

    @Schema(description = "Username for the new user. Must be at least 3 characters long.", example = "john_doe")
    @Size(min = 3, message = "Username must be at least 3 characters long.")
    @NotBlank(message = "Username cannot be blank.")
    private String username;

    @Schema(description = "Password for the new user. Must be at least 6 characters long.", example = "strongpassword123")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    @NotBlank(message = "Password cannot be blank.")
    private String password;

    @Schema(description = "Email address of the new user. Must be a valid email format.", example = "john.doe@example.com")
    @Email(message = "Email must be valid.")
    @NotBlank(message = "Email cannot be blank.")
    private String email;

    @Schema(description = "Phone number for the new user. Must be in the format +375XXXXXXXXX.", example = "+375291234567")
    @Pattern(regexp = "^[+]375[0-9]{9}$", message = "Phone number must be in format +375XXXXXXXXX.")
    @NotBlank(message = "Phone number cannot be blank.")
    private String phoneNumber;

    @Schema(description = "Confirm password to verify the password entered by the user.", example = "strongpassword123")
    @NotBlank(message = "Confirm cannot be blank.")
    private String confirmPassword;
}
