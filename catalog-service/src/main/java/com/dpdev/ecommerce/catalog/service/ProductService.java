package com.dpdev.ecommerce.catalog.service;

import com.dpdev.ecommerce.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public boolean hasProductsInCategory(UUID categoryId) {
        return productRepository.existsByCategoryId(categoryId);
    }
}
