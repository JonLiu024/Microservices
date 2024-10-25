package com.jonltech.funding_management_service.repository;

import com.jonltech.funding_management_service.model.FundingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FundingManagementRepository extends JpaRepository<FundingStatus, Long> {

    List<FundingStatus> findByWildlifeIdIn(List<String> wildlifeIds);
    FundingStatus findByWildlifeId(String wildlifeId);

}
