package com.jonltech.donation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DonationLineItemsDto {
    private String wildlifeId;
    private BigDecimal amount;
}
