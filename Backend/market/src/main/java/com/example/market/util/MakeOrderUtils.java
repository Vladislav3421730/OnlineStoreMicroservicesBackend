package com.example.market.util;

import com.example.market.dto.AddressDto;
import com.example.market.dto.OrderDto;
import com.example.market.dto.OrderRequestDto;
import com.example.market.mapper.OderItemCartMapper;
import com.example.market.mapper.OrderMapper;
import com.example.market.model.Address;
import com.example.market.model.Order;
import com.example.market.model.User;
import com.example.market.model.enums.Status;
import com.example.market.repository.AddressRepository;
import com.example.market.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;



@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MakeOrderUtils {

    AddressRepository addressRepository;
    OderItemCartMapper oderItemCartMapper;
    OrderMapper orderMapper;
    UserRepository userRepository;

    public Order createOrder(OrderRequestDto orderRequestDto) {
        BigDecimal totalPrice = BigDecimal.valueOf(orderRequestDto.getTotalCoast());
        AddressDto addressDto = orderRequestDto.getAddress();

        OrderDto orderDto = OrderDto.builder()
                .totalPrice(totalPrice)
                .address(addressDto)
                .build();

        orderDto.setStatus(Status.ACCEPTED.getDisplayName());
        return orderMapper.toEntity(orderDto);
    }

    public void processAddress(User user, Order order) {
        log.info("Processing address: {}", order.getAddress());

        List<Address> addresses = user.getOrders().stream()
                .map(Order::getAddress)
                .toList();

        if (!addresses.contains(order.getAddress())) {
            log.info("Added new address {} to DB", order.getAddress());
            order.getAddress().setId(null);
            addressRepository.save(order.getAddress());
        }
    }

    public void createOrderItems(User user, Order order) {
        order.setOrderItems(user.getCarts().stream()
                .map(cart -> oderItemCartMapper.map(cart, order))
                .toList());
    }

    public void finalizeOrder(User user, Order order) {
        user.getCarts().clear();
        user.addOrderToList(order);
        userRepository.save(user);
    }

}
