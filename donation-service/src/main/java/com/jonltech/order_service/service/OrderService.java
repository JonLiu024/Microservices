package com.jonltech.order_service.service;

import com.jonltech.order_service.dto.*;
import com.jonltech.order_service.event.OrderPlacedEvent;
import com.jonltech.order_service.model.Order;
import com.jonltech.order_service.model.OrderLineItems;
import com.jonltech.order_service.repository.OrderRepository;
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
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemDtosList()
                .stream().map(this::mapOrderLineItemDtoToPojo).collect(toList());
        order.setOrderLineItemsList(orderLineItemsList);

        Map<String, Integer> orderLineItemQuantities = new HashMap<>();
        for (OrderLineItems orderLineItems: orderLineItemsList) {
            orderLineItemQuantities.put(orderLineItems.getSkuCode(), orderLineItems.getQuantity());
        }

        List<String> skuCodes = orderLineItemsList.stream().map(
                OrderLineItems::getSkuCode).toList();


        Span newSpan = this.tracer.nextSpan().name("placeOrder");
        try (Tracer.SpanInScope ws = this.tracer.withSpan(newSpan.start())) {


            //make request to the inventory service to check if orderlineitems are in stock
            //uriBuilder.queryparam creates the uri in the form localhost:8082/api/inventory?skuCode=..&skuCode2=...
            //-> localhost:8082/api/inventory?skuCode={skuCode1}.. and localhost:8082/api/inventory?skuCode={skuCode}....


            InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                    .uri("http:inventory-services/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build()
                    )
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();


            System.out.println("Inventory check responses: " + Arrays.toString(inventoryResponses));

            assert inventoryResponses != null;
            boolean hasEnoughProductsInstock = Arrays.stream(inventoryResponses).allMatch(inventoryResponse ->
                    inventoryResponse.getStock() >=
                            orderLineItemQuantities.get(inventoryResponse.getSkuCode()));

            if (hasEnoughProductsInstock) {
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
                List<UpdateInventoryRequest> updateInventoryRequestList =
                        orderLineItemsList.stream().map(orderLineItems -> new UpdateInventoryRequest(
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
                    orderRepository.delete(order);
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

    public OrderLineItems mapOrderLineItemDtoToPojo(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        return orderLineItems;

    }


    public List<OrderResponse> getAllOrders() {

        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapOrderToDto).toList();
    }

    public OrderResponse mapOrderToDto(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderNumber(order.getOrderNumber());
        List<OrderLineItemsDto> orderLineItemsDtoList = order.getOrderLineItemsList().stream().map(this::mapOrderLineItemPojoToDto).toList();
        orderResponse.setOrderLineItemsDtoList(orderLineItemsDtoList);
        return orderResponse;
    }

    public OrderLineItemsDto mapOrderLineItemPojoToDto(OrderLineItems orderLineItems) {
        OrderLineItemsDto orderLineItemsDto = new OrderLineItemsDto();
        orderLineItemsDto.setPrice(orderLineItems.getPrice());
        orderLineItemsDto.setQuantity(orderLineItems.getQuantity());
        orderLineItemsDto.setSkuCode(orderLineItems.getSkuCode());
        return orderLineItemsDto;
    }

}
