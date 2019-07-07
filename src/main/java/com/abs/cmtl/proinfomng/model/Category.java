package com.abs.cmtl.proinfomng.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "category")
public class Category {

    @Id
    private String name;
    private String description;
    private String provider;
    private long quantity;
    //For Daily statistics
    @CreatedDate
    private Date createdDate;
    //For Daily statistics
    @LastModifiedDate
    private Date lastModifiedDate;
}
