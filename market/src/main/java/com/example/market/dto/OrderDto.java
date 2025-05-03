package com.example.market.dto;

import com.example.market.annotation.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for order details")
public class OrderDto {

    @Schema(description = "Unique identifier for the order", example = "1")
    private Long id;

    @Schema(description = "Timestamp when the order was created", example = "2025:02:01 14:30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime createdAt;

    @DecimalMin(value = "0.01", message = "Total price must be more or equal than 0.01")
    @Schema(description = "Total price of the order", example = "150.75")
    private BigDecimal totalPrice;

    @Schema(description = "Address associated with the order")
    private AddressDto address;

    @NotNull(message = "User must be not null")
    @Schema(description = "ID of the user who placed the order", example = "101")
    private Long userId;

    @Schema(description = "Email of the user who placed the order", example = "user@example.com")
    private String email;

    @OrderStatus
    @Schema(description = "Current status of the order", example = "PENDING")
    private String status;

    @Schema(description = "List of items included in the order")
    private List<OrderItemDto> orderItems;
}

