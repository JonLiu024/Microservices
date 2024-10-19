package com.jonltech.funding_management_service.repository;

import com.jonltech.funding_management_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findBySkuCodeIn(List<String> skuCodes);
    Inventory findBySkuCode(String skuCode);

}