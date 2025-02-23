package com.example.market.service.Impl;

import com.example.market.dto.OrderDto;
import com.example.market.exception.OrderNotFoundException;
import com.example.market.i18n.I18nUtil;
import com.example.market.mapper.OrderMapper;
import com.example.market.model.Order;
import com.example.market.repository.OrderRepository;
import com.example.market.service.OrderService;
import com.example.market.util.Messages;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;
    I18nUtil i18nUtil;

    @Override
    public Page<OrderDto> findAll(PageRequest pageRequest) {
        log.info("Fetching all orders");
        return orderRepository.findAll(pageRequest)
                .map(orderMapper::toDTO);
    }

    @Override
    public OrderDto findById(Long id) {
        log.info("Fetching order by ID: {}", id);
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new OrderNotFoundException(i18nUtil.getMessage(Messages.ORDER_ERROR_NOT_FOUND, String.valueOf(id))));
        log.info("Found order with ID: {}", id);
        return orderMapper.toDTO(order);
    }

    @Override
    public Page<OrderDto> findAllByUserEmail(String email, PageRequest pageRequest) {
        log.info("Fetching orders for user with email: {}", email);
        return orderRepository.findAllByUserEmail(email, pageRequest)
                .map(orderMapper::toDTO);
    }

    @Override
    public Page<OrderDto> findAllByUserId(Long id, Pageable pageRequest) {
        log.info("Fetching all by user id");
        return orderRepository.findAllByUserId(id, pageRequest)
                .map(orderMapper::toDTO);
    }

    @Override
    @Transactional
    public void update(OrderDto orderDto) {
        log.info("Updating order with ID: {}", orderDto.getId());
        if (!orderRepository.existsById(orderDto.getId())) {
            log.error("Order with id {} not found", orderDto.getId());
            throw new OrderNotFoundException(i18nUtil.getMessage(Messages.ORDER_ERROR_NOT_FOUND, String.valueOf(orderDto.getId())));
        }
        Order order = orderMapper.toEntity(orderDto);
        orderRepository.save(order);
        log.info("Order with ID: {} updated successfully", orderDto.getId());
    }
}
