package com.jonltech.product_services.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WildlifeResponse {
    private String id;
    private String nickName;
    private String description;
    private String conservationStatus;
    private String species;
    private BigDecimal fundingGoal;
}
