package com.jonltech.funding_management_service.controller;

import com.jonltech.funding_management_service.dto.FundingStatusResponse;
import com.jonltech.funding_management_service.dto.UpdateInventoryRequest;
import com.jonltech.funding_management_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor

public class InventoryController {

    private final InventoryService inventoryService;

    //http://localhost:8082/api/inventory?skuCode=?&skuCode=?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FundingStatusResponse> isInStock(@RequestParam(name = "skuCode") List<String> skuCodes) throws InterruptedException {

        return inventoryService.getStockInfo(skuCodes);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateInventory(@RequestBody List<UpdateInventoryRequest> updateInventoryRequests) {
        inventoryService.updateInventoryForOrderPlaced(updateInventoryRequests);

    }




}
