package com.example.market.repository;


import com.example.market.dto.ProductFilterDto;
import com.example.market.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CustomProductRepository {
    Page<Product> findAll(ProductFilterDto productFilterDTO, PageRequest pageRequest);
    void deleteProductWithOrderItems(Long productId);
}
