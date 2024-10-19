package com.jonltech.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    @JsonAlias({"skuCode", "sku_code"})
    private String skuCode;
    private Integer stock;
}
