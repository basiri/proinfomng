package com.abs.cmtl.proinfomng.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "product")
public class Product {
    @Id
    private String uuid;
    @NotNull
    private String name;
    private String description;
    private String provider;
    private boolean available;
    private String measurementUnits;
}
