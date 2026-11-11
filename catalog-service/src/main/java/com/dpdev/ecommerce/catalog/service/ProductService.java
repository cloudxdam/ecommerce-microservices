package com.dpdev.ecommerce.catalog.service;

import com.dpdev.ecommerce.catalog.category.Category;
import com.dpdev.ecommerce.catalog.exception.ResourceNotFoundException;
import com.dpdev.ecommerce.catalog.product.Product;
import com.dpdev.ecommerce.catalog.repository.CategoryRepository;
import com.dpdev.ecommerce.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public boolean hasProductsInCategory(UUID categoryId) {
        return productRepository.existsByCategoryId(categoryId);
    }

    public Product changeProductCategory(UUID productId, UUID categoryId) {

        Product updatedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Product with id %s not found", productId)));

        Category newCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Category %s not found", categoryId)));

        updatedProduct.changeCategory(categoryId);

        return productRepository.save(updatedProduct);
    }
}
