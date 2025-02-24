package com.example.repositoryTests;

import com.example.factory.ProductFactory;
import com.example.market.MarketApplication;
import com.example.market.model.Product;
import com.example.market.repository.ProductRepository;
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
public class ProductRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres");

    @Autowired
    private ProductRepository productRepository;

    private static Product product;
    private static Product invalidProduct;

    @BeforeAll
    static void setup() {
        product = ProductFactory.createValidProduct();
        invalidProduct = ProductFactory.createInvalidProduct();
    }

    @Test
    @Order(1)
    @DisplayName("Test save product")
    @Rollback
    void testSaveProduct() {
        Product savedProduct = productRepository.save(product);
        assertNotNull(savedProduct.getId());
        assertEquals(product.getTitle(), savedProduct.getTitle());
    }

    @Test
    @Order(2)
    @DisplayName("Test save product with invalid data")
    @Rollback
    void testSaveProductWithInvalidData() {
        assertThrows(TransactionSystemException.class, () -> productRepository.save(invalidProduct));
    }


    @Test
    @Order(3)
    @DisplayName("Test find product by id")
    void testFindById() {
        Product foundProduct = productRepository.findById(5L).orElseThrow();
        assertEquals(foundProduct.getId(), 5);
        assertEquals(foundProduct.getTitle(), "Пылесос Bosch");
        assertEquals(foundProduct.getCategory(), "Бытовая техника");
    }

    @Test
    @Order(4)
    @DisplayName("Test find all products")
    void testFindAll() {
        Page<Product> products = productRepository.findAll(PageRequest.of(0, 10));
        assertNotNull(products);
        assertEquals(products.getSize(), 10);
    }

    @Test
    @Order(5)
    @DisplayName("Test delete product")
    @Rollback
    void testDeleteProduct() {
        productRepository.deleteById(5L);
        assertFalse(productRepository.findById(5L).isPresent());
    }

    @Test
    @Order(6)
    @DisplayName("Test update Product")
    @Rollback
    void testUpdateProduct() {
        Product product = productRepository.findById(1L).orElse(null);
        assertNotNull(product);
        product.setTitle("New title");

        productRepository.save(product);
        Product updatedProduct = productRepository.findById(1L).orElse(null);
        assertNotNull(product);
        assertEquals(updatedProduct.getTitle(),"New title");

    }
}
