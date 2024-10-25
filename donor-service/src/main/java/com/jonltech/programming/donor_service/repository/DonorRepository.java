package com.jonltech.programming.donor_service.repository;

import com.jonltech.programming.donor_service.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorRepository extends JpaRepository<Donor, Long> {
}
