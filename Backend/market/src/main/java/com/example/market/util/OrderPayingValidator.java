package com.example.market.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Random;

@UtilityClass
@Slf4j
public class OrderPayingValidator {

    public boolean validateOrderCoast(BigDecimal coast) {

        double doubleValue = coast.doubleValue();

        double max = doubleValue * 1.27;
        double min = doubleValue * 0.95;

        Random random = new Random();

        double checkCoast = random.nextDouble(max - min + 1) + min;
        log.info("values Cost for inspection: {}, Initial cost: {}",checkCoast,doubleValue);
        return checkCoast >= doubleValue;
    }
}
