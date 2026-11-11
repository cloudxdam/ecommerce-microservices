package com.dpdev.ecommerce.catalog.product;

import com.dpdev.ecommerce.catalog.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

    @Test
    void createDraft_withUnit_withSalesTerms_shouldThrowException()  {

        Price price = new Price(new BigDecimal("485"), PricingUnit.UNIT);
        SalesTerms salesTerms = new SalesTerms(new BigDecimal("2"), 1);

        assertThrows(
                BusinessRuleException.class,
                () -> Product.createDraft(
                    "LEN-LAP-IDE5-16G-512S",
                    "Lenovo IdeaPad 5",
                    "Laptop with 16GB RAM, 512 GB SSD and Ryzen 5000",
                    UUID.randomUUID(),
                    price,
                    salesTerms
                )
        );
    }

    @Test
    void createDraft_withKilogram_withoutSalesTerms_shouldThrowException() {

        Price price = new Price(new BigDecimal("140"), PricingUnit.KILOGRAM);

        assertThrows(
                BusinessRuleException.class,
                () -> Product.createDraft(
                    "CAN-BAN-PGI-SPA",
                    "Canary Banana",
                    "Banana from Canary Islands, Spain, Protected Geographical Indication",
                    UUID.randomUUID(),
                    price,
                    null
                )
        );
    }
}
