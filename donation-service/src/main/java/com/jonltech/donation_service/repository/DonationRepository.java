package com.jonltech.donation_service.repository;

import com.jonltech.donation_service.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}
