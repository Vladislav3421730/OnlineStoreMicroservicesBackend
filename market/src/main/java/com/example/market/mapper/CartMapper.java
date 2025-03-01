package com.example.market.mapper;

import com.example.market.dto.CartDto;
import com.example.market.dto.ProductDto;
import com.example.market.model.Cart;
import com.example.market.model.Product;
import com.example.market.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartMapper {

    ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "user",target = "userId")
    CartDto toDTO(Cart cart);

    @Mapping(source = "userId",target = "user")
    Cart toEntity(CartDto cartDto);

    default Product mapProductDtoToProduct(ProductDto productDto){
        return productMapper.toEntity(productDto);
    }

    default ProductDto mapProductToProductDto(Product product){
        return productMapper.toDTO(product);
    }

    default Long mapUserToCartDtoFromCart(User user){
        return user.getId();
    }

    default User mapUserToCartFromCartDto(Long value){
        return User.builder().id(value).build();
    }

}
