package com.example.market.service.Impl;

import com.example.market.dto.*;
import com.example.market.exception.ProductNotFoundException;
import com.example.market.i18n.I18nUtil;
import com.example.market.mapper.ImageMapper;
import com.example.market.mapper.MultipartFileMapper;
import com.example.market.mapper.ProductMapper;
import com.example.market.model.Image;
import com.example.market.model.Product;
import com.example.market.repository.ProductRepository;
import com.example.market.service.ImageService;
import com.example.market.service.ProductService;
import com.example.market.util.Messages;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {

    ImageService imageService;
    ProductRepository productRepository;
    ProductMapper productMapper;
    ImageMapper imageMapper;
    I18nUtil i18nUtil;

    @Override
    @Transactional
    public ProductDto save(CreateProductDto createProductDTO, List<MultipartFile> files) {
        log.info("Save product {}", createProductDTO);
        Product product = productMapper.toNewEntity(createProductDTO);

        if (files != null) {
            List<CreateImageDto> images = files.stream()
                    .filter(file -> !file.isEmpty())
                    .map(file -> {
                        CreateImageDto createImageDto = MultipartFileMapper.map(file);
                        imageService.sendfile(file, createImageDto.getFilePath());
                        return createImageDto;
                    })
                    .toList();

            product.setImageList(new ArrayList<>());
            for (CreateImageDto imageDto : images) {
                Image image = imageMapper.toNewEntity(imageDto);
                product.addImageToList(image);
            }
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Override
    public Page<ProductDto> findAll(ProductFilterDto productFilterDto, PageRequest pageRequest) {
        log.info("Fetching all products");
        return productRepository.findAll(productFilterDto, pageRequest)
                .map(productMapper::toDTO);
    }

    @Override
    public ProductDto findById(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException(i18nUtil.getMessage(Messages.PRODUCT_ERROR_NOT_FOUND, String.valueOf(id))));
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDto update(UpdateProductDto productDto) {

        Product product = productRepository.findById(productDto.getId()).orElseThrow(() ->
                new ProductNotFoundException(i18nUtil.getMessage(Messages.PRODUCT_ERROR_NOT_FOUND, String.valueOf(productDto.getId()))));
        log.info("Updating product with id: {}", productDto.getId());

        product.setTitle(productDto.getTitle());
        product.setCategory(product.getCategory());
        product.setDescription(product.getDescription());
        product.setCoast(product.getCoast());
        product.setAmount(productDto.getAmount());

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with id: {}", productDto.getId());
        return productMapper.toDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            log.error("Products with id {} not found", id);
            throw new ProductNotFoundException(i18nUtil.getMessage(Messages.PRODUCT_ERROR_NOT_FOUND, String.valueOf(id)));
        }
        log.info("Deleting product with id: {}", id);
        productRepository.deleteProductWithOrderItems(id);
        log.info("Product with id {} deleted successfully", id);
    }

}
