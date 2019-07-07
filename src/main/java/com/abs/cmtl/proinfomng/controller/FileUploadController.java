package com.abs.cmtl.proinfomng.controller;



import com.abs.cmtl.proinfomng.model.Product;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RestController
public class FileUploadController {
    private static final String TOPIC_NAME = "product-info";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public FileUploadController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    private void sendMessage(String msg) {

        kafkaTemplate.send(TOPIC_NAME, msg);
    }
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @GetMapping("/test")
    public @ResponseBody String isRunning(){
        return "OK!!!";
    }

    @PostMapping("/upload")
    public @ResponseBody String handleFileUpload(@RequestParam("file")MultipartFile file){


        if(!file.isEmpty()){
            try {
                byte[] bytes = file.getBytes();
                logger.info("=*=*=> The file opened successfully");
                Gson gson = new Gson();
                List<String> productsStr = Stream.of(new String(bytes, StandardCharsets.UTF_8)
                        .split("\n"))
                        .filter(s -> !s.contains("UUID,Name,Description,provider,available,MeasurementUnits"))//filter the header
                        .map(s -> {
                            String[] parts = s.split(",");
                            Map<String,String> productJson = IntStream.range(0, Product.class.getDeclaredFields().length-1)
                                    .boxed().collect(Collectors.toMap(
                                            i -> Product.class.getDeclaredFields()[i].getName(), i -> parts[i], (a, b) -> b));
                            return gson.toJson(productJson);
                        }).collect(Collectors.toList());


                productsStr.forEach(this::sendMessage);

                return "You successfully uploaded the file" ;

            } catch (Exception e) {
                e.printStackTrace();
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "The File structure does not match.");

            }

        }else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Failed to upload , the file is empty." );

        }
    }



}
