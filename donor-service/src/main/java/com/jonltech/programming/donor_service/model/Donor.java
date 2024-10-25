package com.jonltech.programming.donor_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_donors")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Donor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String username;
    private String name;
    private String email;
    private String phone;


}
