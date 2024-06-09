package com.jonltech.order_service.dto;

public class InventoryResponse {
    private String skuCode;
    private boolean isInStock;


    public boolean isInStock() {
        return this.isInStock;
    }
}
