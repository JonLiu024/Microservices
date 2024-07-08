package com.jonltech.inventory_service.service;

import com.jonltech.inventory_service.dto.InventoryResponse;
import com.jonltech.inventory_service.dto.UpdateInventoryRequest;
import com.jonltech.inventory_service.model.Inventory;
import com.jonltech.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) throws InterruptedException {
        //for testing the time limiter properties in circuit breaker of order service
        try {
            log.info("wait started");
            Thread.sleep(10000);
            log.info("wait ended");
        } catch (RuntimeException e) {
            System.out.println(e.toString());
        }
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()).toList();
    }


    @Transactional
    public void updateInventory(List<UpdateInventoryRequest> updateInventoryRequests) {

        for (UpdateInventoryRequest updateInventoryRequest : updateInventoryRequests) {
            Inventory inventory = inventoryRepository.findBySkuCode(updateInventoryRequest.getSkuCode());
            inventory.setQuantity(inventory.getQuantity() - updateInventoryRequest.getQuantity());
            inventoryRepository.save(inventory);
        }
    }


}
