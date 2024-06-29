package com.jonltech.order_service;

import com.jonltech.order_service.model.Order;
import com.jonltech.order_service.model.OrderLineItems;
import com.jonltech.order_service.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner loadData(OrderRepository orderRepository) {
		//create an order with a single order line item for testing purpose
		return args -> {
			Order iphone13Order = new Order();
			iphone13Order.setOrderNumber("13443");
			List<OrderLineItems> orderLineItemsList = new ArrayList<>();
			OrderLineItems orderLineItems = new OrderLineItems();
			orderLineItems.setPrice(new BigDecimal(23));
			orderLineItems.setQuantity(3);
			orderLineItems.setSkuCode("iphone_13_red");
			orderLineItemsList.add(orderLineItems);
			iphone13Order.setOrderLineItemsList(orderLineItemsList);
			orderRepository.save(iphone13Order);
		};
	}
}
