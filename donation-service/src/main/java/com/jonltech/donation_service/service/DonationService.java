package com.jonltech.donation_service.service;

import com.jonltech.donation_service.dto.*;
import com.jonltech.donation_service.event.DonationCreatedEvent;
import com.jonltech.donation_service.model.Donation;
import com.jonltech.donation_service.model.DonationLineItem;
import com.jonltech.donation_service.repository.DonationRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Data
@RequiredArgsConstructor
@Builder
@Service
@Transactional
@Slf4j
public class DonationService {

    private final DonationRepository donationRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, DonationCreatedEvent> kafkaTemplate;

    public String placeOrder(DonationRequest donationRequest) {
        Donation donation = new Donation();
        donation.setOrderNumber(UUID.randomUUID().toString());

        List<DonationLineItem> orderLineItemsList = donationRequest.getOrderLineItemDtosList()
                .stream().map(this::mapOrderLineItemDtoToPojo).collect(toList());
        donation.setOrderLineItemsList(orderLineItemsList);

        Map<String, BigDecimal> donationLineItemAmounts = new HashMap<>();
        for (DonationLineItem orderLineItems: orderLineItemsList) {
            donationLineItemAmounts.put(orderLineItems.getWildlifeId(), orderLineItems.getAmount());
        }


        List<String> wildlifeIds = orderLineItemsList.stream().map(
                DonationLineItem::getWildlifeId).toList();


        Span newSpan = this.tracer.nextSpan().name("placeOrder");
        try (Tracer.SpanInScope ws = this.tracer.withSpan(newSpan.start())) {


            //make request to the inventory service to check if orderlineitems are in stock
            //uriBuilder.queryparam creates the uri in the form localhost:8082/api/inventory?skuCode=..&skuCode2=...
            //-> localhost:8082/api/inventory?skuCode={skuCode1}.. and localhost:8082/api/inventory?skuCode={skuCode}....


            FundingStatusResponse[] fundingStatusResponses = webClientBuilder.build().get()
                    .uri("http:funding-management-service/api/funding", uriBuilder -> uriBuilder.queryParam("wildlifeId", wildlifeIds).build()
                    )
                    .retrieve()
                    .bodyToMono(FundingStatusResponse[].class)
                    .block();


            System.out.println("Funding status check responses: " + Arrays.toString(fundingStatusResponses));

            assert fundingStatusResponses != null;
            boolean fundingGoalNotMet = Arrays.stream(fundingStatusResponses).allMatch(fundingStatusResponse ->
                    fundingStatusResponse.getFundingNeeded().
                            compareTo(donationLineItemAmounts.get(fundingStatusResponse.getWildlifeId())) == 1 ||
                            fundingStatusResponse.getFundingNeeded().
                                    compareTo(donationLineItemAmounts.get(fundingStatusResponse.getWildlifeId())) == 0
            );

            if (fundingGoalNotMet) {
                donationRepository.save(donation);
                kafkaTemplate.send("notificationTopic", new DonationCreatedEvent(donation.getOrderNumber()));
                List<UpdateFundingRequest> updateFundingRequestList =
                        orderLineItemsList.stream().map(orderLineItems -> new UpdateFundingRequest(
                                orderLineItems.getWildlifeId(),
                                orderLineItems.getAmount())
                        ).toList();

                try {
                    webClientBuilder.build().post().uri("localhost:8081/api/inventory/update")
                            .bodyValue(BodyInserters.fromValue(updateFundingRequestList))
                            .retrieve()
                            .bodyToMono(void.class)
                            .block();
                    System.out.println("Inventory updated");
                    return "placing order successfully";

                } catch (Exception e) {
                    System.out.println("Inventory update is not successful");
                    donationRepository.delete(donation);
                    System.out.println("order is deleted from the repository");
                    return "place order failed";
                }


            } else {
                throw new IllegalArgumentException("The product ordered is not in stock");
            }


        } finally {
            newSpan.end();
        }



    }

    public DonationLineItem mapOrderLineItemDtoToPojo(DonationLineItemsDto donationLineItemsDto) {
        DonationLineItem orderLineItems = new DonationLineItem();
        orderLineItems.setWildlifeId(donationLineItemsDto.getWildlifeId());
        orderLineItems.setAmount(donationLineItemsDto.getAmount());
        return orderLineItems;

    }


    public List<OrderResponse> getAllOrders() {

        List<Donation> donations = donationRepository.findAll();
        return donations.stream().map(this::mapDonationToDto).toList();
    }

    public OrderResponse mapDonationToDto(Donation donation) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderNumber(donation.getOrderNumber());
        List<DonationLineItemsDto> donationLineItemsDtoList = donation.getOrderLineItemsList().stream().map(this::mapDonationLineItemPojoToDto).toList();
        orderResponse.setFundingStatusResponseList(donationLineItemsDtoList);
        return orderResponse;
    }

    public DonationLineItemsDto mapDonationLineItemPojoToDto(DonationLineItem orderLineItems) {
        DonationLineItemsDto donationLineItemsDto = new DonationLineItemsDto();
        donationLineItemsDto.setAmount(orderLineItems.getAmount());
        donationLineItemsDto.setWildlifeId(orderLineItems.getWildlifeId());
        return donationLineItemsDto;
    }

}
