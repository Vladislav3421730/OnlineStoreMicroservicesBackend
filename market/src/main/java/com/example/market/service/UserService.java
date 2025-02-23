package com.example.market.service;


import com.example.market.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UserService {

    UserDto saveUser(RegisterUserDto registerUserDto);

    UserDto getUser();

    UserDto findByEmail(String email);

    UserDto findById(Long id);

    Page<UserDto> findAll(PageRequest pageRequest);

    void addProductToCart(UserDto user, ProductDto product);

    void makeOrder(UserDto userDto, OrderRequestDto orderRequestDto);
}
