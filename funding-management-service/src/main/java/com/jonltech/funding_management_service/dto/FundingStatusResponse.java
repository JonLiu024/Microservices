package com.jonltech.funding_management_service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundingStatusResponse {
    @JsonAlias({"wildlifeId", "wildlife_id"})
    private String wildlifeId;
    private BigDecimal amountFunded;
    private BigDecimal fundingGoal;
}
