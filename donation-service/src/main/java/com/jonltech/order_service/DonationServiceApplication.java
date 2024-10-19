package com.jonltech.order_service;

import com.jonltech.order_service.model.Donation;
import com.jonltech.order_service.model.DonationLineIterm;
import com.jonltech.order_service.repository.DonationRepository;
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
public class DonationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonationServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner loadData(DonationRepository donationRepository) {
		//create an order with a single order line item for testing purpose
		return args -> {
			Donation iphone13Donation = new Donation();
			iphone13Donation.setOrderNumber("13443");
			List<DonationLineIterm> orderLineItemsList = new ArrayList<>();
			DonationLineIterm orderLineItems = new DonationLineIterm();
			orderLineItems.setPrice(new BigDecimal(23));
			orderLineItems.setQuantity(3);
			orderLineItems.setSkuCode("iphone_13_red");
			orderLineItemsList.add(orderLineItems);
			iphone13Donation.setOrderLineItemsList(orderLineItemsList);
			donationRepository.save(iphone13Donation);
		};
	}
}
