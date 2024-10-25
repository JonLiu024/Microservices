package com.jonltech.funding_management_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFundingStatusRequest {
    private String wildlifeId;
    private BigDecimal amountFunded;

}
