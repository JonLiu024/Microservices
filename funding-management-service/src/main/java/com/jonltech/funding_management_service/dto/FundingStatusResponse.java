package com.jonltech.funding_management_service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundingStatusResponse {
    @JsonAlias({"skuCode", "sku_code"})
    private String skuCode;
    private Integer stock;
}
