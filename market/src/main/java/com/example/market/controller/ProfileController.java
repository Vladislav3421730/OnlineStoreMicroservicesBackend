package com.example.market.controller;

import com.example.market.dto.*;
import com.example.market.i18n.I18nUtil;
import com.example.market.service.ProfileService;
import com.example.market.util.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/profile")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
        )
})
@Tag(name = "Profile",description = "Endpoints for managing profile")
public class ProfileController {

    ProfileService profileService;
    I18nUtil i18nUtil;

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get current user", description = "Retrieves details of the currently authenticated user.")
    @ApiResponse(
            responseCode = "200",
            description = "User was found successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
    )
    public ResponseEntity<UserDto> getUser() {
        UserDto userDto = profileService.getUser();
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/update")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update user", description = "Update user with fields username, email, phoneNumber")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User was deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FieldErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UpdateUserDto updateUserDto) {
        UserDto userDto = profileService.updateUser(updateUserDto);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/update/password")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update user's password", description = "Updating user's password")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User's password was updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FieldErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<ResponseDto> updatesUserPassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto) {
        profileService.updatePassword(updatePasswordDto);
        return ResponseEntity.ok(new ResponseDto(i18nUtil.getMessage(Messages.USER_SUCCESS_PASSWORD_UPDATED)));
    }
}
