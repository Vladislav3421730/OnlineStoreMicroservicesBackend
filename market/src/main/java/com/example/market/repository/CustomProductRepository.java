package com.example.market.repository;


import com.example.market.dto.ProductFilterDTO;
import com.example.market.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface CustomProductRepository {
    Page<Product> findAllByFilter(ProductFilterDTO productFilterDTO, PageRequest pageRequest);
    void deleteProductWithOrderItems(Long productId);
}
