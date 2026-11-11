package com.dpdev.ecommerce.catalog.category;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("categories")
public class Category {
    @Id
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private boolean active;
}
