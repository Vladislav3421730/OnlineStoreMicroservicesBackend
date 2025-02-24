package com.example.market.controller;

import com.example.market.dto.AppErrorDto;
import com.example.market.dto.ResponseDto;
import com.example.market.dto.UserDto;
import com.example.market.i18n.I18nUtil;
import com.example.market.service.AdminService;
import com.example.market.service.UserService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/user")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "User", description = "Endpoints for managing users")
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
        )
})
public class UserController {

    UserService userService;
    AdminService adminService;
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
        UserDto userDto = userService.getUser();
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/all")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Find all users", description = "Retrieves a paginated list of all users.")
    @ApiResponse(
            responseCode = "200",
            description = "Find all users (pagination included)"
    )
    public ResponseEntity<Page<UserDto>> findAllUsers(
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy) {
        if (offset == null) offset = 0;
        if (pageSize == null) pageSize = 20;
        if (sortBy == null || sortBy.isEmpty()) sortBy = "id";
        Page<UserDto> users = userService.findAll(PageRequest.of(offset, pageSize, Sort.by(sortBy)));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Find user by ID", description = "Retrieves a user by their unique identifier.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User was found successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id) {
        UserDto user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Find user by email", description = "Retrieves a user by their email address.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User was found successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<UserDto> findUserByEmail(@RequestParam("email") String email) {
        log.info("Trying to find user with email {}", email);
        UserDto user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/bun")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Ban or unban user", description = "Bans or unbans a user based on the provided user details.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User was buned/unbaned successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Can't bun/unbun yourself",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<ResponseDto> bunUser(@RequestBody @Valid UserDto userDto) {
        adminService.bun(userDto);
        return ResponseEntity.ok(new ResponseDto(i18nUtil.getMessage(Messages.USER_SUCCESS_UPDATED)));
    }

    @PutMapping("/manager")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Assign or remove manager role", description = "Adds or removes the manager role from a user.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Role manager was added/removed successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Can't add/remove role manager from yourself",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<ResponseDto> madeManager(@RequestBody @Valid UserDto userDto) {
        adminService.madeManager(userDto);
        return ResponseEntity.ok(new ResponseDto(i18nUtil.getMessage(Messages.USER_SUCCESS_UPDATED)));
    }

    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete user by ID", description = "Deletes a user by their unique identifier.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User was deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Can't delete from yourself",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable Long id) {
        adminService.deleteById(id);
        return ResponseEntity.ok(new ResponseDto(i18nUtil.getMessage(Messages.USER_SUCCESS_DELETED, String.valueOf(id))));
    }
}

