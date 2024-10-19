package com.jonltech.product_services.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WildlifeProfileCreatedEvent {
    String productId;
}
