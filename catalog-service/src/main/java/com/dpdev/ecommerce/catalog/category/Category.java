package com.dpdev.ecommerce.catalog.category;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Document("categories")
public class Category {
    @Id
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private boolean active;
}
