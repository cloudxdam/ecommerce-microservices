package com.dpdev.ecommerce.catalog.repository;

import com.dpdev.ecommerce.catalog.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ProductRepository extends MongoRepository<Product, UUID> {

    boolean existsByCategoryId(UUID categoryId);

    boolean existsBySku(String sku);
}
