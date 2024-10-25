package com.jonltech.programming.donor_service.contoller;

import com.jonltech.programming.donor_service.model.Donor;
import com.jonltech.programming.donor_service.service.DonorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Donor> getAllDonrs() {
        return this.donorService.getAllDonors();


    }
}
