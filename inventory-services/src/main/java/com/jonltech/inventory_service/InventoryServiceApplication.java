package com.jonltech.inventory_service;

import com.jonltech.inventory_service.model.Inventory;
import com.jonltech.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;



@EnableDiscoveryClient
@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            Inventory iphone15Blue = new Inventory();
            iphone15Blue.setQuantity(200);
            iphone15Blue.setSkuCode("Iphone15_Blue");

            Inventory iphone15Gold = new Inventory();
            iphone15Gold.setQuantity(0);
            iphone15Gold.setSkuCode("Iphone15_Gold");

            inventoryRepository.save(iphone15Blue);
            inventoryRepository.save(iphone15Gold);
        };
    }
}
