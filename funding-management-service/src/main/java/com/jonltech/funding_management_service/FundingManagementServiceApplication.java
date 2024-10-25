package com.jonltech.funding_management_service;

import com.jonltech.funding_management_service.model.FundingStatus;
import com.jonltech.funding_management_service.repository.FundingManagementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;


@EnableDiscoveryClient
@SpringBootApplication
public class FundingManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundingManagementServiceApplication.class, args);
	}

    @Bean
    public CommandLineRunner loadData(FundingManagementRepository fundingManagementRepository) {
        return args -> {
            FundingStatus wildlife011FundingStatus = new FundingStatus();
            wildlife011FundingStatus.setWildlifeId("011");
            wildlife011FundingStatus.setAmountFunded(BigDecimal.valueOf(2000.2));
            wildlife011FundingStatus.setFundingGoal(BigDecimal.valueOf(3000));

            FundingStatus wildlife022FundingStatus = new FundingStatus();
            wildlife022FundingStatus.setWildlifeId("022");
            wildlife022FundingStatus.setAmountFunded(BigDecimal.valueOf(22020.4));
            wildlife022FundingStatus.setFundingGoal(BigDecimal.valueOf(22020.4));




            fundingManagementRepository.save(wildlife011FundingStatus);
            fundingManagementRepository.save(wildlife022FundingStatus);


        };
    }
    @Configuration
    public class RedisConfig {
        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            return new LettuceConnectionFactory("redis", 6379);
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory());
            return template;
        }
    }
}
