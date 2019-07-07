package com.abs.cmtl.proinfomng.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileUploadControllerTest {
    @LocalServerPort
    int randomServerPort;
    @Autowired
    TestRestTemplate restTemplate;


    @Before
    public void setUp() throws Exception {
        restTemplate = new TestRestTemplate();
    }

    @Test
    public void isRunning() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/test";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri,String.class);

        int status = responseEntity.getStatusCodeValue();
        String response = responseEntity.getBody();

        assertEquals("Correct Response Status", HttpStatus.OK.value(),status);
        assertEquals("OK!!!",response);
    }

}