package com.jonltech.order_service.repository;

import com.jonltech.order_service.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}
