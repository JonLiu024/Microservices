package com.jonltech.donation_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonltech.donation_service.dto.DonationLineItemsDto;
import com.jonltech.donation_service.dto.DonationRequest;
import com.jonltech.donation_service.repository.DonationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@SpringBootTest
class DonationServiceApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private MockMvc mockMvc;


    static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:5.7.34"));


    static {
        mySQLContainer.start();
    }





    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);

    }

    @Test
	void shouldPlaceOrder() throws Exception {

        DonationRequest donationRequest = getOrderRequest();
        String jsonObj = objectMapper.writeValueAsString(donationRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObj))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, donationRepository.findAll().size());
	}

    public DonationRequest getOrderRequest() {
        DonationRequest donationRequest = new DonationRequest();
        donationRequest.setOrderLineItemDtosList(getOrderLineItemsDtosList());
        return donationRequest;
    }

    public List<DonationLineItemsDto> getOrderLineItemsDtosList() {
        DonationLineItemsDto donationLineItemsDto1 = new DonationLineItemsDto();
        donationLineItemsDto1.setAmount(BigDecimal.valueOf(5000.00));
        donationLineItemsDto1.setWildlifeId("232980nois");
        List<DonationLineItemsDto> list = new ArrayList<>();
        list.add(donationLineItemsDto1);
        return list;
    }
}
