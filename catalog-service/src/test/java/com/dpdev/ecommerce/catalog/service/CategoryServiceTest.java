package com.dpdev.ecommerce.catalog.service;

import com.dpdev.ecommerce.catalog.category.Category;
import com.dpdev.ecommerce.catalog.exception.ResourceAlreadyExistsException;
import com.dpdev.ecommerce.catalog.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    // duplicated slug case
    @Test
    void shouldThrowException_whenSlugAlreadyExists() {

        String name = "digital cameras";
        String slug = "digital-cameras";
        String description = "digital photographic cameras";

        when(categoryRepository.existsBySlug(slug)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                ()-> categoryService.createCategory(
                    name,
                    description
                ));

        verify(categoryRepository, never()).save(any(Category.class));
    }

    // successful case
    @Test
    void shouldCreateCategory () {

        String name = "digital cameras";
        String slug = "digital-cameras";
        String description = "digital photographic cameras";

        when(categoryRepository.existsBySlug(slug)).thenReturn(false);
        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Category category = categoryService.createCategory(
                        name,
                        description
                );

        assertNotNull(category);

        verify(categoryRepository).save(category);
    }
}
