package com.jonltech.funding_management_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "t_inventories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FundingStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String wildlifeId;
    private BigDecimal amountFunded;
    private BigDecimal fundingGoal;
}
