package com.dpdev.ecommerce.catalog.category;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("specification_definitions")
public class SpecificationDefinition {
    @Id
    private UUID id;
    private String name;
    private ValueType valueType;
}
