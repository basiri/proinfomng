package com.abs.cmtl.proinfomng;

import com.abs.cmtl.proinfomng.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EntityScan("com.abs.cmtl.proinfomng.model")
@EnableMongoRepositories("com.abs.cmtl.proinfomng.repository")
public class ProInfoMngApplication {
    @Autowired
    ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProInfoMngApplication.class, args);
    }



}
