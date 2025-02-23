package com.example.market.mapper;


import com.example.market.dto.CreateImageDto;
import com.example.market.dto.ImageDto;
import com.example.market.model.Image;
import com.example.market.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper
public interface ImageMapper {

    @Mapping(source = "product",target = "productId")
    ImageDto toDTO(Image image);

    @Mapping(source = "productId",target = "product")
    Image toEntity(ImageDto imageDto);

    Image toNewEntity(CreateImageDto createImageDto);

    default Long mapProductFromImageToImageDto(Product product){
        return product.getId();
    }

    default Product mapProductFromImageDtoToImage(Long value){
        return Product.builder().id(value).build();
    }


}
