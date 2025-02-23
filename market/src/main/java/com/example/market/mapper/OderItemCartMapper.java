package com.example.market.mapper;


import com.example.market.model.Cart;
import com.example.market.model.Order;
import com.example.market.model.OrderItem;
import com.example.market.model.Product;
import com.example.market.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class OderItemCartMapper {

    ProductRepository productRepository;

    public OrderItem map(Cart cart, Order order){
        Product product = cart.getProduct();
        product.setAmount(cart.getProduct().getAmount() - cart.getAmount());
        productRepository.save(product);
        return OrderItem.builder()
                .product(cart.getProduct())
                .amount(cart.getAmount())
                .order(order)
                .build();
    }
}
