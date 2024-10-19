package com.jonltech.product_services.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WildlifeRequest {
    private String id;
    private String name;
    private String species;
    private String conservationStatus;
    private String description;
    private BigDecimal price;
}
