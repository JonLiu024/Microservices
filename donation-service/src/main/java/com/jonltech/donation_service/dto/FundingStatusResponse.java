package com.jonltech.donation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundingStatusResponse {
    private String wildlifeId;
    private BigDecimal fundingNeeded;



}
