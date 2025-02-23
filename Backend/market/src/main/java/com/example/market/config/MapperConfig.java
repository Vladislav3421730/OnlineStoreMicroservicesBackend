package com.example.market.config;

import com.example.market.mapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public UserMapper userMapper() {
        return new UserMapperImpl();
    }

    @Bean
    public ProductMapper productMapper() {
        return new ProductMapperImpl();
    }

    @Bean
    public OrderMapper orderMapper() {
        return new OrderMapperImpl();
    }

    @Bean
    public CartMapper cartMapper() {
        return new CartMapperImpl();
    }

    @Bean
    public AddressMapper addressMapper() {
        return new AddressMapperImpl();
    }

    @Bean
    public ImageMapper imageMapper() {
        return new ImageMapperImpl();
    }


}
