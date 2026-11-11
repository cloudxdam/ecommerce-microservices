package com.dpdev.ecommerce.catalog.service;

import com.dpdev.ecommerce.catalog.category.Category;
import com.dpdev.ecommerce.catalog.exception.ResourceAlreadyExistsException;
import com.dpdev.ecommerce.catalog.exception.ResourceNotFoundException;
import com.dpdev.ecommerce.catalog.product.Price;
import com.dpdev.ecommerce.catalog.product.Product;
import com.dpdev.ecommerce.catalog.product.SalesTerms;
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

    public Product createDraft(String sku,
                               String name,
                               String description,
                               UUID categoryId,
                               Price price,
                               SalesTerms salesTerms
                               ){

        Product newProduct = Product.createDraft(
                sku,
                name,
                description,
                categoryId,
                price,
                salesTerms
        );

        if (productRepository.existsBySku(sku.trim())) {
            throw new ResourceAlreadyExistsException(String.format("SKU %s already exists", sku));
        }

        if (categoryRepository.findById(categoryId).isEmpty()) {
            throw new ResourceNotFoundException(String.format("Category with id %s not found", categoryId));
        }

        return productRepository.save(newProduct);
    }
}
