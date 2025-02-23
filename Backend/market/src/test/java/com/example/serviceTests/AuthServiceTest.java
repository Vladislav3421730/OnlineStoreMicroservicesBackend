package com.example.serviceTests;

import com.example.factory.UserFactory;
import com.example.market.dto.*;
import com.example.market.exception.LoginFailedException;
import com.example.market.exception.RefreshTokenException;
import com.example.market.exception.RegistrationFailedException;
import com.example.market.i18n.I18nUtil;
import com.example.market.mapper.UserMapper;
import com.example.market.model.User;
import com.example.market.repository.UserRepository;
import com.example.market.service.Impl.AuthServiceImpl;
import com.example.market.service.UserService;
import com.example.market.util.JwtAccessTokenUtils;
import com.example.market.util.JwtRefreshTokenUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtAccessTokenUtils jwtAccessTokenUtils;
    @Mock
    private JwtRefreshTokenUtils jwtRefreshTokenUtils;
    @Mock
    private I18nUtil i18nUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private static LoginUserDto loginUserDto;
    private static RegisterUserDto registerUserDto;

    @BeforeEach
    void setup() {
        loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("user@gmail.com");
        loginUserDto.setPassword("password");
        registerUserDto = UserFactory.createRegisterUserDto();
    }

    @Test
    @Order(1)
    @DisplayName("Test create authentication token")
    void testCreateAuthToken() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
        when(userRepository.findByEmail(loginUserDto.getEmail())).thenReturn(Optional.of(new User()));
        when(jwtAccessTokenUtils.generateAccessToken(any(User.class))).thenReturn("Access Token");
        when(jwtRefreshTokenUtils.generateRefreshToken(loginUserDto.getEmail())).thenReturn("Refresh Token");

        JwtResponseDto jwtResponseDto = authService.createAuthToken(loginUserDto);
        assertNotNull(jwtResponseDto.getAccessToken());
        assertNotNull(jwtResponseDto.getRefreshToken());
        assertEquals(jwtResponseDto.getAccessToken(), "Access Token");
        assertEquals(jwtResponseDto.getRefreshToken(), "Refresh Token");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(loginUserDto.getEmail());
        verify(jwtAccessTokenUtils).generateAccessToken(any(User.class));
        verify(jwtRefreshTokenUtils).generateRefreshToken(loginUserDto.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("Test create authentication token with invalid credentials")
    void testCreateAuthTokenWithInvalidCredentials() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        assertThrows(LoginFailedException.class, () -> authService.createAuthToken(loginUserDto));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userRepository);
        verifyNoInteractions(jwtAccessTokenUtils);
        verifyNoInteractions(jwtRefreshTokenUtils);
    }

    @Test
    @Order(3)
    @DisplayName("Test successful refresh token")
    void testRefreshToken() {

        String validRefreshToken = "validRefreshToken";
        String email = "user@gmail.com";
        TokenRefreshRequestDto tokenRefreshRequestDto = new TokenRefreshRequestDto(validRefreshToken);
        User user = new User();
        user.setEmail(email);

        when(jwtRefreshTokenUtils.isRefreshTokenValid(validRefreshToken)).thenReturn(true);
        when(jwtRefreshTokenUtils.getEmailFromRefreshToken(validRefreshToken)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtAccessTokenUtils.generateAccessToken(user)).thenReturn("newAccessToken");

        JwtResponseDto jwtResponseDto = authService.refreshToken(tokenRefreshRequestDto);

        assertNotNull(jwtResponseDto);
        assertEquals("newAccessToken", jwtResponseDto.getAccessToken());
        assertEquals(validRefreshToken, jwtResponseDto.getRefreshToken());

        verify(jwtRefreshTokenUtils).isRefreshTokenValid(validRefreshToken);
        verify(jwtRefreshTokenUtils).getEmailFromRefreshToken(validRefreshToken);
        verify(userRepository).findByEmail(email);
        verify(jwtAccessTokenUtils).generateAccessToken(user);
    }

    @Test
    @Order(4)
    @DisplayName("Test refresh token with invalid token")
    void testRefreshTokenWithInvalidToken() {

        String validRefreshToken = "validRefreshToken";
        TokenRefreshRequestDto tokenRefreshRequestDto = new TokenRefreshRequestDto(validRefreshToken);

        when(jwtRefreshTokenUtils.isRefreshTokenValid(validRefreshToken)).thenReturn(false);

        assertThrows(RefreshTokenException.class, () -> authService.refreshToken(tokenRefreshRequestDto));

        verify(jwtRefreshTokenUtils).isRefreshTokenValid(validRefreshToken);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(jwtAccessTokenUtils);
        verify(jwtRefreshTokenUtils, never()).getEmailFromRefreshToken(any(String.class));

    }

    @Test
    @Order(5)
    @DisplayName("Test register user")
    void testRegisterUser() {

        User user = userMapper.toNewEntity(registerUserDto);
        UserDto userDto = userMapper.toDTO(user);

        when(userService.saveUser(registerUserDto)).thenReturn(userDto);
        UserDto savedUserDto = authService.registerUser(registerUserDto);
        assertNotNull(savedUserDto);
        assertEquals(savedUserDto.getEmail(), registerUserDto.getEmail());
        assertEquals(savedUserDto.getUsername(), registerUserDto.getUsername());
        assertEquals(savedUserDto.getPhoneNumber(), registerUserDto.getPhoneNumber());

        verify(userService).saveUser(registerUserDto);
    }

    @Test
    @Order(6)
    @DisplayName("Test register user with not same passwords")
    void testRegisterUserWithNotSamePasswords() {
        registerUserDto.setConfirmPassword("other password");
        assertThrows(RegistrationFailedException.class, () -> authService.registerUser(registerUserDto));
        verifyNoInteractions(userRepository);
        verifyNoInteractions(userService);


    }

    @Test
    @Order(7)
    @DisplayName("Test register user with not unique email")
    void testRegisterUserWithNotUniqueEmail() {

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(new User()));
        assertThrows(RegistrationFailedException.class, () -> authService.registerUser(registerUserDto));
        verifyNoInteractions(userService);
    }

    @Test
    @Order(8)
    @DisplayName("Test register user with not unique phone number")
    void testRegisterUserWithNotUniquePhoneNumber() {
        when(userRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.of(new User()));
        assertThrows(RegistrationFailedException.class, () -> authService.registerUser(registerUserDto));
        verifyNoInteractions(userService);
    }


}
