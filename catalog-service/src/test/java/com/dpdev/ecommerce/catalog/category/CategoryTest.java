package com.dpdev.ecommerce.catalog.category;

import com.dpdev.ecommerce.catalog.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    void shouldThrowException_whenNameIsNull() {
        assertThrows(BusinessRuleException.class,
                ()-> Category.create(null, "digital photographic cameras")
        );
    }

    @Test
    void shouldThrowException_whenNameIsBlank() {
        assertThrows(BusinessRuleException.class,
                ()-> Category.create("", "digital photographic cameras")
                );
    }

    @Test
    void shouldThrowException_whenNameCannotProduceSlug() {
        assertThrows(BusinessRuleException.class,
                ()-> Category.create("!¿*", "digital photographic cameras")
                );
    }

    @Test
    void shouldCreateCategory_withCorrectSlug() {
       Category category = Category.create("dígital __ cámeras", null);

        assertEquals("digital-cameras", category.getSlug());
    }

    @Test
    void shouldCreateCategory_withBlankDescription() {
        Category category = Category.create("digital cameras", "");

        assertEquals("digital-cameras", category.getSlug());
        assertNull(category.getDescription());
    }
}
