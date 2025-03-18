package com.example.serviceTests;

import com.example.market.dto.UserDto;
import com.example.market.exception.UserNotFoundException;
import com.example.market.i18n.I18nUtil;
import com.example.market.mapper.UserMapper;
import com.example.market.model.User;
import com.example.market.repository.UserRepository;
import com.example.market.service.Impl.ProfileServiceImpl;
import com.example.market.service.ProfileService;
import com.example.market.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private I18nUtil i18nUtil;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private static UserDto userDto;
    private static User user;

    @BeforeAll
    static void setup() {
        userDto = new UserDto();
        userDto.setEmail("user@gmail.com");
        userDto.setUsername("user");

        user = new User();
        user.setEmail("user@gmail.com");
        user.setPassword("encodedPassword");
    }

    @Test
    @Order(4)
    @DisplayName("Test get user by authentication")
    void testGetUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(Boolean.TRUE);
        when(authentication.getPrincipal()).thenReturn(user.getEmail());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.findByEmail(user.getEmail())).thenReturn(userDto);

        UserDto fetchedUser = profileService.getUser();

        assertNotNull(fetchedUser);
        assertEquals(user.getEmail(), fetchedUser.getEmail());

        verify(userService).findByEmail(user.getEmail());
    }

    @Test
    @Order(5)
    @DisplayName("Test get user by authentication with null authentication")
    void testGetUserWithNullAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
        assertThrows(UserNotFoundException.class, () -> profileService.getUser());
    }


    @Test
    @Order(6)
    @DisplayName("Test get user by authentication - user not found")
    void testGetUserUserNotFound() {

        String invalidEmail = "invalidEmail";
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(Boolean.TRUE);
        when(authentication.getPrincipal()).thenReturn(invalidEmail);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.findByEmail(invalidEmail)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> profileService.getUser());

        verify(userService).findByEmail(invalidEmail);
    }


}
