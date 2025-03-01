package com.example.market.mapper;

import com.example.market.dto.CreateProductDto;
import com.example.market.dto.ImageDto;
import com.example.market.dto.ProductDto;
import com.example.market.model.Image;
import com.example.market.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    ImageMapper imageMapper = Mappers.getMapper(ImageMapper.class);

    Product toNewEntity(CreateProductDto dto);

    ProductDto toDTO(Product product);

    Product toEntity(ProductDto productDto);

    default List<Image> mapImageDtosToImages(List<ImageDto> images){
        if(images==null){
            return null;
        }
        return images.stream()
                .map(imageMapper::toEntity)
                .collect(Collectors.toList());
    }

    default List<ImageDto> mapImagesToImageDtos(List<Image> images){
        if(images==null){
            return null;
        }
        return images.stream()
                .map(imageMapper::toDTO)
                .collect(Collectors.toList());
    }

}