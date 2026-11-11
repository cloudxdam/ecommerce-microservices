package com.dpdev.ecommerce.catalog.repository;

import com.dpdev.ecommerce.catalog.category.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface CategoryRepository extends MongoRepository<Category, UUID> {
}
