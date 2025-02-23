package com.example.market.service;


import com.example.market.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> findAll(PageRequest pageRequest);

    OrderDto findById(Long id);

    Page<OrderDto> findAllByUserEmail(String email, PageRequest pageRequest);

    Page<OrderDto> findAllByUserId(Long id, Pageable pageRequest);

    void update(OrderDto order);
}
