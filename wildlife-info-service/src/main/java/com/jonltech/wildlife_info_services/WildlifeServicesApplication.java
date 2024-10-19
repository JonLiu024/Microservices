package com.jonltech.wildlife_info_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WildlifeServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(WildlifeServicesApplication.class, args);
	}

}
