package com.jonltech.inventory_service.service;

import com.jonltech.inventory_service.dto.InventoryResponse;
import com.jonltech.inventory_service.dto.UpdateInventoryRequest;
import com.jonltech.inventory_service.model.Inventory;
import com.jonltech.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {

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
