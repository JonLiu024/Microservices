package com.jonltech.funding_management_service.controller;

import com.jonltech.funding_management_service.dto.FundingStatusResponse;
import com.jonltech.funding_management_service.dto.UpdateFundingStatusRequest;
import com.jonltech.funding_management_service.service.FundingManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor

public class FundingManagementController {

    private final FundingManagementService fundingManagementService;

    //http://localhost:8082/api/inventory?skuCode=?&skuCode=?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FundingStatusResponse> isInStock(@RequestParam(name = "skuCode") List<String> skuCodes) throws InterruptedException {

        return fundingManagementService.getFundingStatusInfo(skuCodes);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateInventory(@RequestBody List<UpdateFundingStatusRequest> updateFundingStatusRequests) {
        fundingManagementService.updateFundingStatusForDonationPlaced(updateFundingStatusRequests);

    }


}
