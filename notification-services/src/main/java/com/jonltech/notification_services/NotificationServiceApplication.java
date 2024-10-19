package com.jonltech.notification_services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic", groupId = "notificationId")
    public void handleEvent(Object event) {
        if (event instanceof DonationMadeEvent orderPlacedEvent) {
            log.info("Received notification for order placing -- {}", orderPlacedEvent.getOrderNumber());
        } else if (event instanceof WildlifeProfileCreatedEvent productCreatedEvent) {
            log.info("Received notification for product created -- {}", productCreatedEvent.getProductId());
        } else {
            WildlifeFundingUpdatedEvent inventoryUpdateEvent = (WildlifeFundingUpdatedEvent) event;
            log.info("Received notification for inventory update -- {}", inventoryUpdateEvent.getSkuCode());
        }

    }


}

