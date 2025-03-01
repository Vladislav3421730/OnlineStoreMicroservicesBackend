package com.example.market.util;

import com.example.market.model.Cart;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@UtilityClass
@Slf4j
public class CheckAmount {

    public boolean check(Cart cart) {
        if (cart.getAmount() + 1 > cart.getProduct().getAmount()) {
            log.error("The quantity of goods in the basket is equal to the quantity in the warehouse, " +
                            "it is impossible to increase the quantity of goods {}. Available quantity in warehouse: {}",
                    cart.getProduct().getTitle(), cart.getProduct().getAmount());
            return true;
        }
        return false;
    }
}
