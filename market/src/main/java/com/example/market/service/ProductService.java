package com.example.market.service;

import com.example.market.dto.CreateProductDto;
import com.example.market.dto.ProductDto;
import com.example.market.dto.ProductFilterDto;
import com.example.market.dto.UpdateProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductDto save(CreateProductDto createProductDTO, List<MultipartFile> files);

    Page<ProductDto> findAll(ProductFilterDto productFilterDto, PageRequest pageRequest);

    ProductDto findById(Long id);

    ProductDto update(UpdateProductDto updateProductDto);

    void delete(Long id);

}
