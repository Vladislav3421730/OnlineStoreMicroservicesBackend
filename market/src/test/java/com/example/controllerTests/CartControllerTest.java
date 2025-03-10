package com.example.controllerTests;

import com.example.factory.ProductFactory;
import com.example.market.MarketApplication;
import com.example.market.dto.AddressDto;
import com.example.market.dto.LoginUserDto;
import com.example.market.dto.OrderRequestDto;
import com.example.market.dto.ProductDto;
import com.example.market.service.ProductService;
import com.example.market.util.OrderPayingValidator;
import com.example.utils.TokenUtils;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(classes = MarketApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private ProductService productService;

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
    private static LoginUserDto loginUserDto;
    private static ProductDto productDtoWithZeroAmount;
    private static OrderRequestDto orderRequestDto;

    @BeforeAll
    static void setup() {
        environment.start();

        AddressDto addressDto = AddressDto.builder()
                .id(1L)
                .town("Москва")
                .region("Московская область")
                .exactAddress("ул. Тверская, д. 1")
                .postalCode("125009")
                .build();

        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setTotalCoast(500.0D);
        orderRequestDto.setAddress(addressDto);
        productDtoWithZeroAmount = ProductFactory.createProductDtoWithZeroAmount();
        loginUserDto = new LoginUserDto("user@gmail.com", "q1w2e3");
    }

    @AfterAll
    static void teardown() {
        environment.stop();
    }

    @Test
    @Order(1)
    @DisplayName("Test add product to user's cart")
    void testAddProductToCart() throws Exception {

        String accessToken = TokenUtils.getAccessTokenFromRequest(mockMvc, loginUserDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart/add/{id}", 1L)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    @Test
    @Order(2)
    @DisplayName("Test increment product in user cart")
    void testIncrementProductInUserCart() throws Exception {

        String accessToken = TokenUtils.getAccessTokenFromRequest(mockMvc, loginUserDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cart/increment/{index}", 0L)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    @Test
    @Order(3)
    @DisplayName("Test increment product in user cart with wrong index")
    void testIncrementProductInUserCartWithWrongIndex() throws Exception {

        String accessToken = TokenUtils.getAccessTokenFromRequest(mockMvc, loginUserDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cart/increment/{index}", Integer.MAX_VALUE)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.code", is(404)));
    }

    @Test
    @Order(4)
    @DisplayName("Test decrement product in user cart")
    void testDecrementProductInUserCart() throws Exception {

        String accessToken = TokenUtils.getAccessTokenFromRequest(mockMvc, loginUserDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cart/decrement/{index}", 0L)

                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    @Test
    @Order(5)
    @DisplayName("Test delete product in user cart")
    void testDeleteProductInUserCart() throws Exception {

        String accessToken = TokenUtils.getAccessTokenFromRequest(mockMvc, loginUserDto);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cart/delete/{index}", 0L)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    @Test
    @Order(6)
    @DisplayName("Test make order")
    void testMakeOrder() throws Exception {
        try (MockedStatic<OrderPayingValidator> mockedStatic = Mockito.mockStatic(OrderPayingValidator.class)) {
            mockedStatic.when(() -> OrderPayingValidator.validateOrderCoast(
                    BigDecimal.valueOf(orderRequestDto.getTotalCoast()))).thenReturn(Boolean.TRUE);

            String accessToken = TokenUtils.getAccessTokenFromRequest(mockMvc, loginUserDto);

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderRequestDto))
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message", notNullValue()));
        }
    }

    @Test
    @Order(6)
    @DisplayName("Test make order when payment failed")
    void testMakeOrderWithFailedPayment() throws Exception {
        try (MockedStatic<OrderPayingValidator> mockedStatic = Mockito.mockStatic(OrderPayingValidator.class)) {
            mockedStatic.when(() -> OrderPayingValidator.validateOrderCoast(
                    BigDecimal.valueOf(orderRequestDto.getTotalCoast()))).thenReturn(Boolean.FALSE);

            String accessToken = TokenUtils.getAccessTokenFromRequest(mockMvc, loginUserDto);

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderRequestDto))
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", notNullValue()))
                    .andExpect(jsonPath("$.code", is(400)));
        }
    }

}
