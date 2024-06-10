package com.jonltech.inventory_service.controller;

import com.jonltech.inventory_service.dto.InventoryResponse;
import com.jonltech.inventory_service.service.InventoryService;
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
    public List<InventoryResponse> isInStock(@RequestParam(name = "skuCode") List<String> skuCodes) {

        return inventoryService.isInStock(skuCodes);
    }

}
