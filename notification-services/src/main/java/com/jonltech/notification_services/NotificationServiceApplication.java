package com.jonltech.notification_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener
    public void handlePlaceOrderEvent(OrderPlacedEvent orderPlacedEvent) {

    }

    @KafkaListener
    public void handleCreateProductEvent(ProductCreatedEvent productCreatedEvent) {

    }

    @KafkaListener
    public void handleInventoryUpdateEvent(InventoryUpdateEvent inventoryUpdateEvent) {

    }

}

