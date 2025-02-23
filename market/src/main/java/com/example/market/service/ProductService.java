package com.example.market.service;

import com.example.market.dto.CreateProductDto;
import com.example.market.dto.ProductDto;
import com.example.market.dto.ProductFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    void save(CreateProductDto createProductDTO, List<MultipartFile> files);

    Page<ProductDto> findAll(PageRequest pageRequest);

    ProductDto findById(Long id);

    Page<ProductDto> findAllByTitle(String title, PageRequest pageRequest);

    Page<ProductDto> findAllByFilter(ProductFilterDTO productFilterDTO, PageRequest pageRequest);

    ProductDto update(ProductDto product);

    void delete(Long id);

}
