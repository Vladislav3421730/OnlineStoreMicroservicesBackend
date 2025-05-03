package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a user with associated information like username, email, phone number, and orders")
public class UserDto {

    @Schema(description = "Unique identifier for the user", example = "1")
    private Long id;

    @Schema(description = "Username of the user. Minimum length is 3 characters.", example = "john_doe")
    @Size(min = 3, message = "Username length must be more or equal than 3")
    private String username;

    @Schema(description = "Password for the user. Minimum length is 6 characters.", example = "password123")
    @Size(min = 6, message = "Password length must be more or equal than 6 ")
    private String password;

    @Schema(description = "Email address of the user. Must contain '@'.", example = "johndoe@example.com")
    @Email(message = "Email must contains @")
    private String email;

    @Schema(description = "Indicates whether the user is banned", example = "false")
    @NotNull
    private boolean bun;

    @Schema(description = "User's phone number in the format +375XXXXXXXXX", example = "+375291234567")
    @Column(name = "phone_number", unique = true)
    @Pattern(regexp = "^[+]375[0-9]{9}$", message = "Phone number must be in format +375XXXXXXXXX")
    private String phoneNumber;

    private Boolean isLoyal;

    @Schema(description = "Set of roles assigned to the user", example = "[\"USER\", \"ADMIN\"]")
    private Set<String> roleSet;

    @Schema(description = "List of carts associated with the user")
    private List<CartDto> carts;

    @Schema(description = "List of orders made by the user")
    private List<OrderDto> orders;

}
