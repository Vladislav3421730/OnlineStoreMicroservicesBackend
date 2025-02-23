package com.example.market.service;


import com.example.market.dto.CartDto;

import java.util.List;

public interface CartService {

    boolean incrementAmountOfCartInBasket(List<CartDto> userCarts, int index);

    void decrementAmountOfCartInBasket(List<CartDto> userCarts, int index);

    void deleteCartFromBasket(List<CartDto> cartAfterRemoving, int index);
}
