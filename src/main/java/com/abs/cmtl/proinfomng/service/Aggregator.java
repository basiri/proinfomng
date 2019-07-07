package com.abs.cmtl.proinfomng.service;

import com.abs.cmtl.proinfomng.model.Category;
import com.abs.cmtl.proinfomng.model.Product;
import com.abs.cmtl.proinfomng.repository.CategoryRepository;
import com.abs.cmtl.proinfomng.repository.ProductRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class Aggregator {
    private static final Logger logger = LoggerFactory.getLogger(Aggregator.class);

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @KafkaListener(topics = "product-info", groupId = "test")
    public void listen(String message) {
        //System.out.println("Received Message in group test: " + message);
        Gson gson = new Gson();
        Product product = productRepository.save(gson.fromJson(message,Product.class));
        logger.info("*=*=*>Saving product =+=+=+==>" +product.toString());
        categorySync(product);


    }

    private void categorySync(Product product) {
        Optional<Category> category = categoryRepository.findById(product.getName());
        if(category.isPresent()){
            category.get().setQuantity(category.get().getQuantity()+1);
            category.get().setLastModifiedDate(new Date());
        }else{
            category = Optional.of(new Category(product.getName(), product.getDescription(),
                    product.getProvider(), 1,new Date(),new Date()));
        }
        categoryRepository.save(category.get());
        logger.info("*=*=*>Saving Category =+=+=+>" + category.toString());
    }

}
