package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for updating user")
public class UpdateUserDto {

    @Schema(description = "Unique identifier for the user", example = "1")
    @NotNull(message = "id must be not null")
    @Min(value = 1, message = "id must be greater than or equal to 1")
    private Long id;

    @Schema(description = "Username of the user. Minimum length is 3 characters.", example = "john_doe")
    @Size(min = 3, message = "Username length must be more or equal than 3")
    private String username;

    @Schema(description = "Email address of the user. Must contain '@'.", example = "johndoe@example.com")
    @Email(message = "Email must contains @")
    private String email;

    @Schema(description = "User's phone number in the format +375XXXXXXXXX", example = "+375291234567")
    @Column(name = "phone_number", unique = true)
    @Pattern(regexp = "^[+]375[0-9]{9}$", message = "Phone number must be in format +375XXXXXXXXX")
    private String phoneNumber;

}
