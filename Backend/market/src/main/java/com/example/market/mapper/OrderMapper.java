package com.example.market.mapper;


import com.example.market.dto.OrderDto;
import com.example.market.dto.OrderItemDto;
import com.example.market.model.Order;
import com.example.market.model.OrderItem;
import com.example.market.model.User;
import com.example.market.model.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface OrderMapper {

    OrderItemMapper orderItemMapper = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(source = "userId",target = "user")
    Order toEntity(OrderDto orderDto);

    @Mapping(source = "user",target = "userId")
    @Mapping(source = "user",target = "email")
    OrderDto toDTO(Order Order);

    default List<OrderItemDto> mapOrderItemsListToOrderItemDtoList(List<OrderItem> orderItems){
        if(orderItems==null){
            return null;
        }
        return orderItems.stream()
                .map(orderItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    default List<OrderItem> mapOrderItemsListDtoToOrderItemList(List<OrderItemDto> orderItems){
        if(orderItems==null){
            return null;
        }
        return orderItems.stream()
                .map(orderItemMapper::toEntity)
                .collect(Collectors.toList());
    }

    default Status mapStatusFromOrderDtoToOrder(String status){
        return switch (status) {
            case "принят" -> Status.ACCEPTED;
            case "в обработке" -> Status.IN_PROCESSING;
            case "в пути" -> Status.IN_TRANSIT;
            case "доставлен" -> Status.DELIVERED;
            default -> null;
        };
    }

    default Long mapToOrderDtoFromOrder(User user){
        return user.getId();
    }

    default String mapEmailToOrderDtoFromOrder(User user){
        return user.getEmail();
    }

    default User mapToOrderFromOrderDto(Long userId){
        return User.builder().id(userId).build();
    }

    default String mapStatusToOrderDtoFromOrder(Status status) {
        return status.getDisplayName();
    }
}
