package com.example.market.service;

import com.example.market.dto.UpdatePasswordDto;
import com.example.market.dto.UpdateUserDto;
import com.example.market.dto.UserDto;

public interface ProfileService {

    UserDto updateUser(UpdateUserDto updateUserDto);

    UserDto getUser();

    void updatePassword(UpdatePasswordDto updatePasswordDto);
}
