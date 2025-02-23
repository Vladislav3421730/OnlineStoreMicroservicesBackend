package com.example.market.mapper;

import com.example.market.dto.OrderItemDto;
import com.example.market.dto.ProductDto;
import com.example.market.model.Order;
import com.example.market.model.OrderItem;
import com.example.market.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface OrderItemMapper {

    ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "order",target = "orderId")
    OrderItemDto toDTO(OrderItem order);

    @Mapping(source = "orderId",target = "order")
    OrderItem toEntity(OrderItemDto orderItemDto);

    default Product mapProductDtoToProduct(ProductDto productDto){
        return productMapper.toEntity(productDto);
    }

    default ProductDto mapProductToProductDto(Product product){
        return productMapper.toDTO(product);
    }

    default Order mapOrderFromOrderItemDtoToOrderItem(Long value){
        return Order.builder().id(value).build();
    }

    default Long mapOrderFromOrderItemDtoToOrderItem(Order order){
        return order.getId();
    }
}
