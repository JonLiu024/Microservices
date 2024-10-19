package com.jonltech.order_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonltech.order_service.dto.OrderLineItemsDto;
import com.jonltech.order_service.dto.OrderRequest;
import com.jonltech.order_service.repository.OrderRepository;
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
class OrderServiceApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;
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

        OrderRequest orderRequest = getOrderRequest();
        String jsonObj = objectMapper.writeValueAsString(orderRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObj))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, orderRepository.findAll().size());
	}

    public OrderRequest getOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItemDtosList(getOrderLineItemsDtosList());
        return orderRequest;
    }

    public List<OrderLineItemsDto> getOrderLineItemsDtosList() {
        OrderLineItemsDto orderLineItemsDto1 = new OrderLineItemsDto();
        orderLineItemsDto1.setPrice(BigDecimal.valueOf(5000.00));
        orderLineItemsDto1.setQuantity(3);
        orderLineItemsDto1.setSkuCode("232980nois");
        List<OrderLineItemsDto> list = new ArrayList<>();
        list.add(orderLineItemsDto1);
        return list;
    }
}
