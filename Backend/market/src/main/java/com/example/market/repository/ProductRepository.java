package com.example.market.repository;


import com.example.market.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {

    Page<Product> findAllByTitleContainingIgnoreCase(String title, PageRequest pageRequest);
}
