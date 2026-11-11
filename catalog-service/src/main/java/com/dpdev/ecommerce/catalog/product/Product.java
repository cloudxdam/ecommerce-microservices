package com.dpdev.ecommerce.catalog.product;

import com.dpdev.ecommerce.catalog.exception.BusinessRuleException;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document("products")
public class Product {
    @Id
    private UUID id;
    private String sku;
    private String name;
    private String description;
    private UUID categoryId;
    private ProductStatus status;
    private Price price;
    private Instant createdAt;
    private Instant updatedAt;
    private SalesTerms salesTerms;

    private Product(String sku,
                    String name,
                    String description,
                    UUID categoryId,
                    Price price,
                    SalesTerms salesTerms) {

        if (sku == null || sku.isBlank()) {
            throw new BusinessRuleException("SKU cannot be blank");
        }

        if (name == null || name.isBlank()) {
            throw new BusinessRuleException("Name cannot be blank");
        }

        if (description == null || description.isBlank()) {
            throw new BusinessRuleException("Description cannot be blank");
        }

        if (categoryId == null) {
            throw new BusinessRuleException("CategoryId cannot be null");
        }

        if (price == null) {
            throw new BusinessRuleException("Price cannot be null");
        }

        if (price.getPricingUnit() == PricingUnit.KILOGRAM) {
            if (salesTerms == null) {
                throw new BusinessRuleException("Sales terms must be specified when pricing unit is kilogram");
            }
        }

        if (price.getPricingUnit() == PricingUnit.UNIT && salesTerms != null) {
            throw new BusinessRuleException("Sales terms are only applicable to kilogram pricing");
        }

        this.id = UUID.randomUUID();
        this.sku = sku.trim();
        this.name = name.trim();
        this.description = description.trim();
        this.categoryId = categoryId;
        this.status = ProductStatus.DRAFT;
        this.price = price;
        this.salesTerms = salesTerms;
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public static Product createDraft(
            String sku,
            String name,
            String description,
            UUID categoryId,
            Price price,
            SalesTerms salesTerms
    ) {
        return new Product(sku, name, description, categoryId, price, salesTerms);
    }

    public void activate() {
        if (status == ProductStatus.DRAFT || status == ProductStatus.INACTIVE) {
            status = ProductStatus.ACTIVE;
            updatedAt = Instant.now();
        } else {
            throw new BusinessRuleException("Product only can be activated when it is draft or inactive");
        }
    }

    public void deactivate() {
        if (status != ProductStatus.ACTIVE) {
            throw new BusinessRuleException("The product is not active");
        }

        this.status = ProductStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public void changeSku(String newSku) {
        if (status != ProductStatus.DRAFT) {
            throw new BusinessRuleException("SKU can only be changed while the product is draft");
        }

        if (newSku == null || newSku.isBlank()) {
            throw new BusinessRuleException("New SKU cannot be blank");
        }

        this.sku = newSku.trim();
        this.updatedAt = Instant.now();
    }

    public void changeName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new BusinessRuleException("New name cannot be blank");
        }

        this.name = newName.trim();
        this.updatedAt = Instant.now();
    }

    public void changeDescription(String newDescription) {
        if (newDescription == null || newDescription.isBlank()) {
            throw new BusinessRuleException("New description cannot be blank");
        }

        this.description = newDescription.trim();
        this.updatedAt = Instant.now();
    }

    public void changeCategory(UUID newCategory) {
        if (newCategory == null) {
            throw new BusinessRuleException("Category ID cannot be null");
        }

        this.categoryId = newCategory;
        this.updatedAt = Instant.now();
    }

    public void changePricing (Price newPrice, SalesTerms newSalesTerms) {
        if (newPrice == null) {
            throw new BusinessRuleException("Price cannot be null");
        }

        if (newPrice.getPricingUnit() == PricingUnit.KILOGRAM && newSalesTerms == null) {
            throw new BusinessRuleException("Sales Terms cannot be null");
        }

        if (newPrice.getPricingUnit() == PricingUnit.UNIT && newSalesTerms != null) {
            throw new BusinessRuleException("Sales terms must only be specified for kilogram pricing unit");
        }

        this.price = newPrice;
        this.salesTerms = newSalesTerms;
        this.updatedAt = Instant.now();
    }
}