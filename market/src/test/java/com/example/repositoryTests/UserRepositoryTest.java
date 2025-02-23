package com.example.repositoryTests;

import com.example.factory.UserFactory;
import com.example.market.MarketApplication;
import com.example.market.model.User;
import com.example.market.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.TransactionSystemException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(classes = MarketApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres");

    @Autowired
    private UserRepository userRepository;

    private static User user;
    private static User invalidUser;

    @BeforeAll
    static void setup() {
        user = UserFactory.createUser();
        invalidUser = UserFactory.createUserWithInvalidData();
    }

    @Test
    @Order(1)
    @DisplayName("Test save user")
    @Rollback
    void testSaveUser() {
        userRepository.save(user);
        assertNotNull(user.getId());
        assertEquals(user.getEmail(), "vlados@gmail.com");
    }

    @Test
    @Order(2)
    @DisplayName("Test save user with invalid data")
    @Rollback
    void testSaveUserWithInvalidData() {
        assertThrows(TransactionSystemException.class,
                () -> userRepository.save(invalidUser));
    }

    @Test
    @Order(3)
    @DisplayName("Test find user by ID")
    void testFindById() {
        User foundUser = userRepository.findById(1L).orElse(null);
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        assertEquals("vlad@gmail.com", foundUser.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("Test find all users with pagination")
    void testFindAll() {
        Page<User> page = userRepository.findAll(PageRequest.of(0, 1));

        assertNotNull(page);
        assertTrue(page.getTotalElements() > 0);
        assertEquals(1, page.getSize());
    }

    @Test
    @Order(5)
    @DisplayName("Test find user by email")
    void testFindByEmail() {
        User foundUser = userRepository.findByEmail("vlad@gmail.com").orElse(null);
        assertNotNull(foundUser);
        assertEquals("vlad@gmail.com", foundUser.getEmail());
    }

    @Test
    @Order(6)
    @DisplayName("Test find user by phone number")
    void testFindByPhoneNumber() {
        // Используем пользователя с номером "+375291234567"
        User foundUser = userRepository.findByPhoneNumber("+375291234567").orElse(null);
        assertNotNull(foundUser);
        assertEquals("+375291234567", foundUser.getPhoneNumber());
    }

    @Test
    @Order(7)
    @DisplayName("Test delete user by ID")
    @Rollback
    void testDeleteUser() {
        userRepository.deleteById(5L);
        User foundUser = userRepository.findById(5L).orElse(null);
        assertNull(foundUser, "User should be deleted");
    }

    @Test
    @Order(8)
    @DisplayName("Test update user")
    @Rollback
    void testUpdateUser() {
        User userToUpdate = userRepository.findById(1L).orElseThrow();
        userToUpdate.setEmail("newemail@gmail.com");

        userRepository.save(userToUpdate);

        User updatedUser = userRepository.findById(1L).orElseThrow();
        assertNotNull(updatedUser);
        assertEquals("newemail@gmail.com", updatedUser.getEmail());
    }


}
