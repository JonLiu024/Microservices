package com.jonltech.donation_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DonationCreatedEvent {
    private String orderNumber;
}
