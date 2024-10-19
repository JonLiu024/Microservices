package com.jonltech.wildlife_info_services.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WildlifeProfileCreatedEvent {
    String productId;
}
