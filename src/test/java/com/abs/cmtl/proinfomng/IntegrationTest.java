package com.abs.cmtl.proinfomng;

import com.abs.cmtl.proinfomng.model.Category;
import com.abs.cmtl.proinfomng.model.Product;
import com.abs.cmtl.proinfomng.repository.CategoryRepository;
import com.abs.cmtl.proinfomng.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    ProductRepository repository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TestRestTemplate restTemplate;
    private static final String URL= "http://localhost:8080";


    @Test
    public void isRunningShouldReturnStringOk() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL+"/test",String.class);

        int status = responseEntity.getStatusCodeValue();
        String response = responseEntity.getBody();

        assertEquals("Correct Response Status", HttpStatus.OK.value(),status);
        assertEquals("OK!!!",response);
    }

    @Test
    public void handleFileUploadAndCheckDataBaseUpdate() {

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new ClassPathResource("/input/dataExampleB.csv"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
                map, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/upload"  , HttpMethod.POST, requestEntity,
                String.class);
        int status = responseEntity.getStatusCodeValue();
        String response = responseEntity.getBody();

        assertEquals("Correct Response Status", HttpStatus.OK.value(),status);
        assertEquals( "Correct Message Response","You successfully uploaded the file",response);

        List<Product> products= repository.findAll();
        assertEquals(1000,products.size());
        List<Category> categories= categoryRepository.findAll();
        assertEquals(10,categories.size());


    }

    @Test
    public void AttachEmptyFileAndGetStatus() {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new ClassPathResource("/input/dataExamEmp.csv"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
                map, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/upload"  , HttpMethod.POST, requestEntity,
                String.class);
        int status = responseEntity.getStatusCodeValue();
        String response = responseEntity.getBody();

        assertEquals("Response Status", HttpStatus.BAD_REQUEST.value(),status);
        assertTrue(response.contains("Failed to upload , the file is empty."));

        //assertEquals( "Correct Message Response","Failed to upload , the file is empty.",response);

    }

    @Test
    public void sendingWrongFileExpectError() {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new ClassPathResource("/input/file.txt"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
                map, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/upload"  , HttpMethod.POST, requestEntity,
                String.class);
        int status = responseEntity.getStatusCodeValue();
        String response = responseEntity.getBody();

        assertEquals("Response Status", HttpStatus.BAD_REQUEST.value(),status);

        assertTrue(response.contains("The File structure does not match."));
    }


}