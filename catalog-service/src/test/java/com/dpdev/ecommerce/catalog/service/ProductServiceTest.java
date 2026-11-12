package com.dpdev.ecommerce.catalog.service;

import com.dpdev.ecommerce.catalog.category.Category;
import com.dpdev.ecommerce.catalog.exception.ResourceAlreadyExistsException;
import com.dpdev.ecommerce.catalog.exception.ResourceNotFoundException;
import com.dpdev.ecommerce.catalog.product.Price;
import com.dpdev.ecommerce.catalog.product.PricingUnit;
import com.dpdev.ecommerce.catalog.product.Product;
import com.dpdev.ecommerce.catalog.product.SalesTerms;
import com.dpdev.ecommerce.catalog.repository.CategoryRepository;
import com.dpdev.ecommerce.catalog.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    // duplicated SKU case
    @Test
    void shouldThrowException_WhenSkuAlreadyExists () {

        String sku = "LEN-LAP-IDE5-32G-1024S";
        UUID categoryId = UUID.randomUUID();

        when(productRepository.existsBySku(sku)).thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> productService.createDraft(
                sku,
                "Lenovo IdeaPad 5",
                "Laptop with 16GB RAM, 512 GB SSD and Ryzen 5000",
                categoryId,
                new Price(new BigDecimal("485"), PricingUnit.UNIT),
                null
            )
        );

        verify(categoryRepository, never()).findById(categoryId);
    }

    // successful case
    @Test
    void shouldCreateDraft () {

        String sku = "LEN-LAP-IDE5-32G-1024S";
        UUID categoryId = UUID.randomUUID();

        when(productRepository.existsBySku(sku)).thenReturn(false);
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(new Category()));
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product product = productService.createDraft(
                        sku,
                        "Lenovo IdeaPad 5",
                        "Laptop with 16GB RAM, 512 GB SSD and Ryzen 5000",
                        categoryId,
                        new Price(new BigDecimal("485"), PricingUnit.UNIT),
                        null
                );

        assertNotNull(product);

        verify(productRepository).save(product);
    }

    // no such category case
    @Test
    void shouldThrowException_whenCategoryId_doesNotExists() {

        String sku = "LEN-LAP-IDE5-32G-1024S";
        UUID categoryId = UUID.randomUUID();

        when(productRepository.existsBySku(sku)).thenReturn(false);
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.createDraft(
                        sku,
                        "Lenovo IdeaPad 5",
                        "Laptop with 16GB RAM, 512 GB SSD and Ryzen 5000",
                        categoryId,
                        new Price(new BigDecimal("485"), PricingUnit.UNIT),
                        null
                )
        );

        verify(categoryRepository).findById(categoryId);
        verify(productRepository, never()).save(any(Product.class));
    }
}
