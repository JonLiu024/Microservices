package com.jonltech.funding_management_service.service;

import com.jonltech.funding_management_service.dto.FundingStatusResponse;
import com.jonltech.funding_management_service.dto.UpdateFundingStatusRequest;
import com.jonltech.funding_management_service.event.FundingStatusUpdateEvent;
import com.jonltech.funding_management_service.model.FundingStatus;
import com.jonltech.funding_management_service.repository.FundingManagementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class FundingManagementService {

    private final FundingManagementRepository fundingManagementRepository;
    private final KafkaTemplate<String, FundingStatusUpdateEvent> kafkaTemplate;

    @Transactional(readOnly = true)
    public List<FundingStatusResponse> getFundingStatusInfo(List<String> wildlifeId) throws InterruptedException {
        return fundingManagementRepository.findByWildlifeIdIn(wildlifeId).stream()
                .map(fundingStatus -> FundingStatusResponse.builder()
                        .wildlifeId(fundingStatus.getWildlifeId())
                                .amountFunded(fundingStatus.getAmountFunded())
                        .fundingGoal(fundingStatus.getFundingGoal())
                        .build()).toList();
    }


    @Transactional
    public void updateFundingStatusForDonationPlaced(List<UpdateFundingStatusRequest> updateFundingStatusRequests) {

        for (UpdateFundingStatusRequest updateFundingStatusRequest : updateFundingStatusRequests) {
            FundingStatus fundingStatus = fundingManagementRepository.findByWildlifeId(updateFundingStatusRequest.getWildlifeId());
            fundingStatus.setAmountFunded(fundingStatus.getAmountFunded().add(updateFundingStatusRequest.getAmountFunded()));
            fundingManagementRepository.save(fundingStatus);
            kafkaTemplate.send("notificationTopic", new FundingStatusUpdateEvent(fundingStatus.getWildlifeId()));

        }
    }


}
