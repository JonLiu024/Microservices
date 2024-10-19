package com.jonltech.funding_management_service.service;

import com.jonltech.funding_management_service.dto.FundingStatusResponse;
import com.jonltech.funding_management_service.dto.UpdateInventoryRequest;
import com.jonltech.funding_management_service.event.InventoryUpdateEvent;
import com.jonltech.funding_management_service.model.Inventory;
import com.jonltech.funding_management_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, InventoryUpdateEvent> kafkaTemplate;

    @Transactional(readOnly = true)
    public List<FundingStatusResponse> getStockInfo(List<String> skuCode) throws InterruptedException {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory -> FundingStatusResponse.builder()
                        .skuCode(inventory.getSkuCode())
                        .stock(inventory.getQuantity()).build()).toList();
    }


    @Transactional
    public void updateInventoryForOrderPlaced(List<UpdateInventoryRequest> updateInventoryRequests) {

        for (UpdateInventoryRequest updateInventoryRequest : updateInventoryRequests) {
            Inventory inventory = inventoryRepository.findBySkuCode(updateInventoryRequest.getSkuCode());
            inventory.setQuantity(inventory.getQuantity() - updateInventoryRequest.getQuantity());
            inventoryRepository.save(inventory);
            kafkaTemplate.send("notificationTopic", new InventoryUpdateEvent(inventory.getSkuCode()));

        }
    }


}
