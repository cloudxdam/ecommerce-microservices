package com.dpdev.ecommerce.catalog.category;

import com.dpdev.ecommerce.catalog.exception.BusinessRuleException;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.Normalizer;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Getter
@Document("categories")
public class Category {
    @Id
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean active;

    private Category(
            String name,
            String description
    ) {

        if (name == null || name.isBlank()) {
            throw new BusinessRuleException("Name cannot be blank");
        }

        this.id = UUID.randomUUID();
        this.name = name.trim();
        this.slug = generateSlug(name);

        if (description != null) {
            String trimmedDescription = description.trim();
            this.description = trimmedDescription.isBlank() ? null : trimmedDescription;
        }

        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.active = true;
    }

    public static Category create(
            String name,
            String description
    ) {
        return new Category(name, description);
    }

    private static String generateSlug(String name) {

        String slug =  Normalizer.normalize(name.trim(), Normalizer.Form.NFD).toLowerCase(Locale.ROOT)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");

        if (slug.isBlank()) {
            throw new BusinessRuleException("Cannot generate slug with the provided name");
        }

        return slug;
    }
}
