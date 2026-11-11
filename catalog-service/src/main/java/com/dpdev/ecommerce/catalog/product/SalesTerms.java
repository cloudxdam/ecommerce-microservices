package com.dpdev.ecommerce.catalog.product;

import com.dpdev.ecommerce.catalog.exception.BusinessRuleException;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SalesTerms {
    private final BigDecimal averageWeightPerUnitKg;
    private final int minimumQuantity;

    public SalesTerms(BigDecimal averageWeightPerUnitKg, int minimumQuantity) {

        if (averageWeightPerUnitKg == null) {
            throw new BusinessRuleException("Average weight per unit cannot be null");
        }

        if (averageWeightPerUnitKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Average weight must be greater than 0");
        }

        if (minimumQuantity < 1) {
            throw new BusinessRuleException("Minimum quantity must be equal or greater than 1");
        }

        this.averageWeightPerUnitKg = averageWeightPerUnitKg;
        this.minimumQuantity = minimumQuantity;
    }
}
