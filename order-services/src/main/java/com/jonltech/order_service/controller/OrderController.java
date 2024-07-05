package com.jonltech.order_service.controller;

import com.jonltech.order_service.dto.OrderRequest;
import com.jonltech.order_service.dto.OrderResponse;
import com.jonltech.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return "order created successfully";
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    //fallback logic when the circuit breaker goes to open state
    public String fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        return "Oops, the order cannot be placed at the moment, please try again later!";
    }

}
