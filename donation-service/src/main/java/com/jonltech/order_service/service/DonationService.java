package com.jonltech.order_service.service;

import com.jonltech.order_service.dto.*;
import com.jonltech.order_service.event.DonationCreatedEvent;
import com.jonltech.order_service.model.Donation;
import com.jonltech.order_service.model.DonationLineIterm;
import com.jonltech.order_service.repository.DonationRepository;
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

        List<DonationLineIterm> orderLineItemsList = donationRequest.getOrderLineItemDtosList()
                .stream().map(this::mapOrderLineItemDtoToPojo).collect(toList());
        donation.setOrderLineItemsList(orderLineItemsList);

        Map<String, Integer> orderLineItemQuantities = new HashMap<>();
        for (DonationLineIterm orderLineItems: orderLineItemsList) {
            orderLineItemQuantities.put(orderLineItems.getSkuCode(), orderLineItems.getQuantity());
        }

        List<String> skuCodes = orderLineItemsList.stream().map(
                DonationLineIterm::getSkuCode).toList();


        Span newSpan = this.tracer.nextSpan().name("placeOrder");
        try (Tracer.SpanInScope ws = this.tracer.withSpan(newSpan.start())) {


            //make request to the inventory service to check if orderlineitems are in stock
            //uriBuilder.queryparam creates the uri in the form localhost:8082/api/inventory?skuCode=..&skuCode2=...
            //-> localhost:8082/api/inventory?skuCode={skuCode1}.. and localhost:8082/api/inventory?skuCode={skuCode}....


            FundingServiceResponse[] fundingServiceRespons = webClientBuilder.build().get()
                    .uri("http:inventory-services/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build()
                    )
                    .retrieve()
                    .bodyToMono(FundingServiceResponse[].class)
                    .block();


            System.out.println("Inventory check responses: " + Arrays.toString(fundingServiceRespons));

            assert fundingServiceRespons != null;
            boolean hasEnoughProductsInstock = Arrays.stream(fundingServiceRespons).allMatch(fundingServiceResponse ->
                    fundingServiceResponse.getStock() >=
                            orderLineItemQuantities.get(fundingServiceResponse.getSkuCode()));

            if (hasEnoughProductsInstock) {
                donationRepository.save(donation);
                kafkaTemplate.send("notificationTopic", new DonationCreatedEvent(donation.getOrderNumber()));
                List<UpdateFundingRequest> updateInventoryRequestList =
                        orderLineItemsList.stream().map(orderLineItems -> new UpdateFundingRequest(
                                orderLineItems.getSkuCode(),
                                orderLineItems.getQuantity())
                        ).toList();

                try {
                    webClientBuilder.build().post().uri("localhost:8081/api/inventory/update")
                            .bodyValue(BodyInserters.fromValue(updateInventoryRequestList))
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

    public DonationLineIterm mapOrderLineItemDtoToPojo(DonationLineItemsDto donationLineItemsDto) {
        DonationLineIterm orderLineItems = new DonationLineIterm();
        orderLineItems.setQuantity(donationLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(donationLineItemsDto.getSkuCode());
        orderLineItems.setPrice(donationLineItemsDto.getPrice());
        return orderLineItems;

    }


    public List<OrderResponse> getAllOrders() {

        List<Donation> donations = donationRepository.findAll();
        return donations.stream().map(this::mapOrderToDto).toList();
    }

    public OrderResponse mapOrderToDto(Donation donation) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderNumber(donation.getOrderNumber());
        List<DonationLineItemsDto> donationLineItemsDtoList = donation.getOrderLineItemsList().stream().map(this::mapOrderLineItemPojoToDto).toList();
        orderResponse.setDonationLineItemsDtoList(donationLineItemsDtoList);
        return orderResponse;
    }

    public DonationLineItemsDto mapOrderLineItemPojoToDto(DonationLineIterm orderLineItems) {
        DonationLineItemsDto donationLineItemsDto = new DonationLineItemsDto();
        donationLineItemsDto.setPrice(orderLineItems.getPrice());
        donationLineItemsDto.setQuantity(orderLineItems.getQuantity());
        donationLineItemsDto.setSkuCode(orderLineItems.getSkuCode());
        return donationLineItemsDto;
    }

}
