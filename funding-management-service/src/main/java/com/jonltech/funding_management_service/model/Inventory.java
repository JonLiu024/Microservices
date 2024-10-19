package com.jonltech.funding_management_service.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "t_inventories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private Integer quantity;
}
