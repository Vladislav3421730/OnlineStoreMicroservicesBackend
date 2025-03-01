package com.example.market.service;

import com.example.market.dto.*;

public interface AuthService {

    JwtResponseDto createAuthToken(LoginUserDto user);

    JwtResponseDto refreshToken(TokenRefreshRequestDto tokenRefreshRequestDto);

    UserDto registerUser(RegisterUserDto user);

    void deleteRefreshToken(TokenRefreshRequestDto tokenRefreshRequestDto);


}
