package com.example.controllerTests;

import com.example.factory.UserFactory;
import com.example.market.MarketApplication;
import com.example.market.dto.LoginUserDto;
import com.example.market.dto.RegisterUserDto;
import com.example.market.dto.TokenRefreshRequestDto;
import com.jayway.jsonpath.JsonPath;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.time.Duration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(classes = MarketApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres")
            .withReuse(false);

    @ClassRule
    public static DockerComposeContainer environment =
            new DockerComposeContainer<>(new File("src/test/resources/compose-test.yaml"))
                    .withExposedService("redis", 6379,
                            Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

    @Autowired
    MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static LoginUserDto loginUserDto;
    private static LoginUserDto invalidLoginUserDto;
    private static RegisterUserDto registerUserDto;
    private static RegisterUserDto invalidRegisterUserDto;

    @BeforeAll
    static void setup() {
        environment.start();
        loginUserDto = new LoginUserDto("user@gmail.com", "q1w2e3");
        invalidLoginUserDto = new LoginUserDto("vlad@gmail.com", "invalidPassword");
        registerUserDto = UserFactory.createRegisterUserDtoForControllers();
        invalidRegisterUserDto = UserFactory.createInvalidRegisterUserDtoForControllers();
    }

    @AfterAll
    static void teardown() {
        environment.stop();
    }

    @Test
    @Order(1)
    @DisplayName("Test login and get access and refresh tokens")
    void testCreateAuthToken() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUserDto))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()));
    }

    @Test
    @Order(2)
    @DisplayName("Test login with invalid data")
    void testCreateAuthTokenWithInvalidData() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLoginUserDto))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.code", notNullValue()));
    }

    @Test
    @Order(3)
    @DisplayName("Test register user")
    @Rollback
    void testRegisterUser() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserDto))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpectAll(
                        jsonPath("$.email", is(registerUserDto.getEmail())),
                        jsonPath("$.username", is(registerUserDto.getUsername())),
                        jsonPath("$.phoneNumber", is(registerUserDto.getPhoneNumber()))
                );

    }

    @Test
    @Order(4)
    @DisplayName("Test register user with invalid data")
    @Rollback
    void testRegisterUserWithInvalidData() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRegisterUserDto))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password", notNullValue()));

    }

    @Test
    @Order(5)
    @DisplayName("Test refresh token")
    void testRefreshToken() throws Exception {
        MockHttpServletRequestBuilder loginRequest = MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUserDto))
                .accept(MediaType.APPLICATION_JSON);

        String response = mockMvc.perform(loginRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andReturn().getResponse().getContentAsString();

        String refreshToken = JsonPath.parse(response).read("$.refreshToken");

        TokenRefreshRequestDto tokenRefreshRequestDto =
                new TokenRefreshRequestDto(refreshToken);

        MockHttpServletRequestBuilder refreshRequest = MockMvcRequestBuilders.post("/api/v1/auth/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRefreshRequestDto))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(refreshRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", is(refreshToken)));
    }

    @Test
    @Order(6)
    @DisplayName("Test refresh invalid token")
    void testRefreshInvalidToken() throws Exception {
        String refreshToken = "Invalid token";
        TokenRefreshRequestDto tokenRefreshRequestDto =
                new TokenRefreshRequestDto(refreshToken);

        MockHttpServletRequestBuilder refreshRequest = MockMvcRequestBuilders.post("/api/v1/auth/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRefreshRequestDto))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(refreshRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.code", is(401)));
    }
}

