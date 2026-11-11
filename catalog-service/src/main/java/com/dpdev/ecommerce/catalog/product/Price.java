package com.dpdev.ecommerce.catalog.product;

import com.dpdev.ecommerce.catalog.exception.BusinessRuleException;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Price {
    private BigDecimal amount;
    private PricingUnit pricingUnit;

    public Price(
            BigDecimal amount,
            PricingUnit pricingUnit
    ) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Price must be greater than or equal to 0");
        }

        if (pricingUnit == null) {
            throw new BusinessRuleException("Pricing unit must be specified");
        }

        this.amount = amount;
        this.pricingUnit = pricingUnit;
    }
}
