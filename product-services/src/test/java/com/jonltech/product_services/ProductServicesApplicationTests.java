package com.jonltech.product_services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonltech.product_services.dto.ProductRequest;
import com.jonltech.product_services.dto.ProductResponse;
import com.jonltech.product_services.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServicesApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.2.2");
    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongo.uri", mongoDBContainer::getReplicaSetUrl);
    }

	@Test
	void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String requestJson = objectMapper.writeValueAsString(productRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated());
        Assertions.assertEquals(13, productRepository.findAll().size());

	}

    public ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("iphone 10")
                .description("a regular phone")
                .price(new BigDecimal(555.6)).build();
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get("/api/product")).
                andExpect(status().isOk()).andReturn();
        //convert the mvcResult into a JSON string
        String jsonResults = results.getResponse().getContentAsString();
        List<ProductResponse> allProductResponse = objectMapper.readValue(jsonResults, new TypeReference<List<ProductResponse>>() {
        });
        Assertions.assertEquals(13, allProductResponse.size());

    }



}
