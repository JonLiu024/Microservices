package com.jonltech.donation_service.controller;

import com.jonltech.donation_service.dto.DonationRequest;
import com.jonltech.donation_service.dto.DonationResponse;
import com.jonltech.donation_service.service.DonationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/donate")
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeDonationOrder(@RequestBody DonationRequest donationRequest) {

        return CompletableFuture.supplyAsync(() -> donationService.placeOrder(donationRequest)) ;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DonationResponse> getAllOrders() {
        return donationService.getAllDonations();
    }

    //fallback logic when the circuit breaker goes to open state
    public CompletableFuture<String> fallbackMethod(DonationRequest donationRequest, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Oops, the order cannot be placed at the moment, please try again later!");
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompletableFuture<String> deleteDonationOrder(Long id) {
        return CompletableFuture.supplyAsync(() -> donationService.deleteOrderById(id));
    }

}
