package com.example.market.service.Impl;

import com.example.market.dto.CartDto;
import com.example.market.exception.ExceedingQuantityException;
import com.example.market.exception.WrongIndexException;
import com.example.market.i18n.I18nUtil;
import com.example.market.mapper.CartMapper;
import com.example.market.model.Cart;
import com.example.market.repository.CartRepository;
import com.example.market.service.CartService;
import com.example.market.util.CheckAmount;
import com.example.market.util.Messages;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CartServiceImpl implements CartService {

    CartRepository cartRepository;
    CartMapper cartMapper;
    I18nUtil i18nUtil;

    @Override
    @Transactional
    public boolean incrementAmountOfCartInBasket(List<CartDto> userCarts, int index) {
        if (index < 0 || index >= userCarts.size()) {
            log.error("Index out of bounds: {}", index);
            throw new WrongIndexException(i18nUtil.getMessage(Messages.CART_ERROR_INDEX_OUT_OF_BOUNDS, String.valueOf(index)));
        }
        CartDto cart = userCarts.get(index);
        if (CheckAmount.check(cartMapper.toEntity(cart))) {
            throw new ExceedingQuantityException(i18nUtil.getMessage(Messages.CART_ERROR_QUANTITY_EXCEEDS_STOCK,
                    cart.getProduct().getTitle(), String.valueOf(cart.getProduct().getAmount())));
        }
        cart.setAmount(cart.getAmount() + 1);
        Cart updatedCart = cartMapper.toEntity(cart);
        cartRepository.save(updatedCart);
        log.info("The quantity of product '{}' has been increased by 1. New quantity: {}", cart.getProduct().getTitle(), cart.getAmount());
        return true;
    }

    @Override
    @Transactional
    public void decrementAmountOfCartInBasket(List<CartDto> userCarts, int index) {
        if (index < 0 || index >= userCarts.size()) {
            log.error("Index out of bounds: {}", index);
            throw new WrongIndexException(i18nUtil.getMessage(Messages.CART_ERROR_INDEX_OUT_OF_BOUNDS, String.valueOf(index)));
        }
        CartDto cart = userCarts.get(index);
        if (cart.getAmount() == 1) {
            cartRepository.deleteById(cart.getId());
            log.info("The product '{}' has been removed from the user's cart", cart.getProduct().getTitle());
        } else {
            cart.setAmount(cart.getAmount() - 1);
            Cart updatedCart = cartMapper.toEntity(cart);
            cartRepository.save(updatedCart);
            log.info("The quantity of product '{}' has been reduced by 1. New quantity: {}",
                    cart.getProduct().getTitle(), cart.getAmount());
        }
    }

    @Override
    @Transactional
    public void deleteCartFromBasket(List<CartDto> cartAfterRemoving, int index) {
        if (index < 0 || index >= cartAfterRemoving.size()) {
            log.error("Index out of bounds: {}", index);
            throw new WrongIndexException(i18nUtil.getMessage(Messages.CART_ERROR_INDEX_OUT_OF_BOUNDS, String.valueOf(index)));
        }
        cartRepository.deleteById(cartAfterRemoving.get(index).getId());
    }


}
