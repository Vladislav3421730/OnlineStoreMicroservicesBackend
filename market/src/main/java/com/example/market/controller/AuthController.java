package com.example.market.controller;

import com.example.market.dto.*;
import com.example.market.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Authentication", description = "Endpoints for user authentication and authorization (login, registration, token refresh)")
public class AuthController {

    AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates user and returns a Access and Refresh tokens.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully authenticated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid login credentials",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<JwtResponseDto> createToken(@RequestBody @Valid LoginUserDto userDto) {
        JwtResponseDto jwtResponseDto = authService.createAuthToken(userDto);
        return ResponseEntity.ok(jwtResponseDto);
    }

    @PostMapping("/register")
    @Operation(summary = "Registration", description = "Registers a user and returns the registered user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully registered user"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Registration failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto) {
        UserDto userDto = authService.registerUser(registerUserDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "Refresh token", description = "Refresh token, return access token and sent refresh token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully refreshed token"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid refresh token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<JwtResponseDto> refreshToken(@RequestBody @Valid TokenRefreshRequestDto tokenRefreshRequestDto) {
        JwtResponseDto jwtResponseDto = authService.refreshToken(tokenRefreshRequestDto);
        return ResponseEntity.ok(jwtResponseDto);
    }
}
