package com.jonltech.donation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationResponse {
    private String orderNumber;
    private List<DonationLineItemsDto> donationLineItemsDtos;

}
