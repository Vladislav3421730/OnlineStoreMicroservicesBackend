package com.example.controllerTests;

import com.example.market.MarketApplication;
import com.example.market.dto.LoginUserDto;
import com.example.utils.TokenUtils;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.time.Duration;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringBootTest(classes = MarketApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static LoginUserDto loginUserDtoWithAdminRole;

    @BeforeAll
    static void setup() {
        environment.start();
        loginUserDtoWithAdminRole = new LoginUserDto("vlad@gmail.com", "q1w2e3");
    }

    @AfterAll
    static void teardown() {
        environment.stop();
    }

    @Test
    @Order(1)
    @DisplayName("Test get user")
    void testGetUser() throws Exception {

        String accessToken = TokenUtils.getAccessTokenFromRequest(mockMvc, loginUserDtoWithAdminRole);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("vlad@gmail.com"));
    }

    @Test
    @Order(2)
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test find user by id")
    void testFindUserById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("vlad@gmail.com"));
    }

    @Test
    @Order(3)
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test find all users")
    void testFindAllUsers() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(4));
    }

    @Test
    @Order(4)
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test find user by email")
    void testFindUserByEmail() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/email")
                        .param("email", "vlad@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("vlad@gmail.com"));
    }

    @Test
    @Order(5)
    @DisplayName("Test bun user")
    void testBanUser() throws Exception {

        String accessToken = TokenUtils.getAccessTokenFromRequest(mockMvc, loginUserDtoWithAdminRole);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", 2L)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.email").value("user@gmail.com"))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/bun")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(response)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User was updated successfully"));
    }

    @Test
    @Order(6)
    @DisplayName("Test delete user by id")
    void testDeleteUser() throws Exception {

        String accessToken = TokenUtils.getAccessTokenFromRequest(mockMvc, loginUserDtoWithAdminRole);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/{id}", 2L)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User with id 2 was deleted successfully"));
    }

    @Test
    @Order(7)
    @DisplayName("Test delete yourself")
    void testDeleteYourself() throws Exception {

        String accessToken = TokenUtils.getAccessTokenFromRequest(mockMvc, loginUserDtoWithAdminRole);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/{id}", 1L)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.code", notNullValue()));
    }

    @Test
    @Order(8)
    @DisplayName("Test delete user without role Admin")
    @WithMockUser(roles = {"USER", "MANAGER"})
    void testDeleteUserWithoutRoleAdmin() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/{id}", 1L))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.code", notNullValue()));
    }
}
