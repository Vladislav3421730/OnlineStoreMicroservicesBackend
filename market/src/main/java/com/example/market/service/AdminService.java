package com.example.market.service;

import com.example.market.dto.UserDto;

public interface AdminService {

    void bun(UserDto userDto);

    void madeManager(UserDto userDto);

    void deleteById(Long id);

}
