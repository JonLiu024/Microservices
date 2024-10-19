package com.jonltech.donation_service;

import com.jonltech.donation_service.model.Donation;
import com.jonltech.donation_service.model.DonationLineItem;
import com.jonltech.donation_service.repository.DonationRepository;
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
			Donation WildlifeDonation = new Donation();
			WildlifeDonation.setOrderNumber("13443");
			List<DonationLineItem> donationLineItems = new ArrayList<>();
			DonationLineItem donationLineItem = new DonationLineItem();
			donationLineItem.setAmount(new BigDecimal(23));
			donationLineItem.setWildlifeId("BDG-111");
			donationLineItems.add(donationLineItem);
			WildlifeDonation.setOrderLineItemsList(donationLineItems);
			donationRepository.save(WildlifeDonation);
		};
	}
}
