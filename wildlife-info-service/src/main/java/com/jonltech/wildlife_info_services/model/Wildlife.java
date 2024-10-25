package com.jonltech.wildlife_info_services.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Document(value = "wildlifeProfile")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wildlife {
    @Id
    private String id;
    private String nickName;
    private String species;
    private LocalDate admissionDate;
    private String conservationStatus;
    private String description;
    private BigDecimal fundingGoal;

}
