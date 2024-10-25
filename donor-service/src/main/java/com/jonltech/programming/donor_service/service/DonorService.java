package com.jonltech.programming.donor_service.service;

import com.jonltech.programming.donor_service.model.Donor;
import com.jonltech.programming.donor_service.repository.DonorRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class DonorService {
    private final DonorRepository donorRepository;
    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }
}
