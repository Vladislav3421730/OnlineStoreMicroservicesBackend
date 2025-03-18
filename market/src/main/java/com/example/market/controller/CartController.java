package com.example.market.controller;

import com.example.market.dto.*;
import com.example.market.exception.ExceedingQuantityException;
import com.example.market.exception.NoQuantityProductException;
import com.example.market.exception.PaymentFailedException;
import com.example.market.i18n.I18nUtil;
import com.example.market.service.CartService;
import com.example.market.service.ProductService;
import com.example.market.service.ProfileService;
import com.example.market.service.UserService;
import com.example.market.util.Messages;
import com.example.market.util.OrderPayingValidator;
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

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/cart")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Cart", description = "Endpoints for managing cart items (add, remove, update quantity, etc.)")
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
public class CartController {

    ProductService productService;
    ProfileService profileService;
    UserService userService;
    CartService cartService;
    I18nUtil i18nUtil;

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create an order", description = "Processes the order request and creates an order for the user.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Order created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error with making order",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<ResponseDto> makeOrder(@RequestBody @Valid OrderRequestDto orderRequest) {
        log.info("Received order request: {}", orderRequest);
        UserDto user = profileService.getUser();
        BigDecimal totalPrice = BigDecimal.valueOf(orderRequest.getTotalCoast());
        if (!OrderPayingValidator.validateOrderCoast(totalPrice)) {
            log.error("Paying filed for price {}", totalPrice);
            throw new PaymentFailedException(i18nUtil.getMessage(Messages.CART_ERROR_LOW_BALANCE));
        }
        userService.makeOrder(user, orderRequest);
        return ResponseEntity.ok(new ResponseDto(i18nUtil.getMessage(Messages.CART_SUCCESS_ORDER_CREATED)));
    }

    @PostMapping("/add/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add product to cart", description = "Adds a product to the user's cart by product ID.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product added successfully to cart"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error during adding product",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<ResponseDto> addProductToCart(@PathVariable Long id) {
        ProductDto productDto = productService.findById(id);
        if (productDto.getAmount() == 0) {
            throw new NoQuantityProductException(i18nUtil.getMessage(Messages.CART_ERROR_NO_QUANTITY_PRODUCT, productDto.getTitle()));
        }
        UserDto userDto = profileService.getUser();
        userService.addProductToCart(userDto, productDto);
        return ResponseEntity.ok(new ResponseDto(i18nUtil.getMessage(Messages.CART_SUCCESS_PRODUCT_ADDED, productDto.getTitle(), userDto.getEmail())));
    }

    @PutMapping("/increment/{index}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Increment product quantity", description = "Increases the quantity of a product in the user's cart by index.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product in cart was incremented successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error during incrementing product in cart",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<ResponseDto> incrementAmountOfProduct(@PathVariable Integer index) {
        UserDto user = profileService.getUser();
        if (!cartService.incrementAmountOfCartInBasket(user.getCarts(), index)) {
            throw new ExceedingQuantityException(i18nUtil.getMessage(Messages.CART_ERROR_EXCEEDING_QUANTITY));
        }
        return ResponseEntity.ok(new ResponseDto(i18nUtil.getMessage(Messages.CART_SUCCESS_CART_UPDATED)));
    }

    @PutMapping("/decrement/{index}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Decrement product quantity", description = "Decreases the quantity of a product in the user's cart by index.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product in cart was decremented successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error during decrementing product in cart",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<ResponseDto> decrementAmountOfProduct(@PathVariable Integer index) {
        UserDto user = profileService.getUser();
        cartService.decrementAmountOfCartInBasket(user.getCarts(), index);
        return ResponseEntity.ok(new ResponseDto(i18nUtil.getMessage(Messages.CART_SUCCESS_CART_UPDATED)));
    }


    @DeleteMapping("/delete/{index}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete product from cart", description = "Removes a product from the user's cart by index.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product in cart was deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error during deleting product in cart",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<ResponseDto> deleteProductFromCart(@PathVariable Integer index) {
        UserDto user = profileService.getUser();
        cartService.deleteCartFromBasket(user.getCarts(), index);
        return ResponseEntity.ok(new ResponseDto(i18nUtil.getMessage(Messages.CART_SUCCESS_CART_DELETED)));
    }
}
