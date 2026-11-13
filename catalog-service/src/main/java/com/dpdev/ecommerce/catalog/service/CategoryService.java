package com.dpdev.ecommerce.catalog.service;

import com.dpdev.ecommerce.catalog.category.Category;
import com.dpdev.ecommerce.catalog.exception.ResourceAlreadyExistsException;
import com.dpdev.ecommerce.catalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(
            String name,
            String description
    ) {
        Category newCategory = Category.create(
                name,
                description
        );

        String slug = newCategory.getSlug();
        if (categoryRepository.existsBySlug(slug)) {
            throw new ResourceAlreadyExistsException(String.format("Cannot create category with name %s. Slug already exists", name));
        }

        return categoryRepository.save(newCategory);
    }
}
