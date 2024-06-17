package com.jonltech.order_service.service;

import com.jonltech.order_service.dto.InventoryResponse;
import com.jonltech.order_service.dto.OrderLineItemsDto;
import com.jonltech.order_service.dto.OrderRequest;
import com.jonltech.order_service.dto.OrderResponse;
import com.jonltech.order_service.model.Order;
import com.jonltech.order_service.model.OrderLineItems;
import com.jonltech.order_service.repository.OrderRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@Builder
@Service
public class OrderService {

    public final OrderRepository orderRepository;
    public final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemDtosList()
                .stream().map(this::mapOrderLineItemDtoToPojo).collect(Collectors.toList());
        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode).collect(Collectors.toList());


        //make request to the inventory service to check if orderlineitems are in stock
        //uriBuilder.queryparam creates the uri in the form localhost:8082/api/inventory?skuCode=..&skuCode2=...
        //-> localhost:8082/api/inventory?skuCode={skuCode1}.. and localhost:8082/api/inventory?skuCode={skuCode}....


        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build()
                )
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        System.out.println("Inventory check responses: " + Arrays.toString(inventoryResponses));

        boolean allProductsInstock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if (allProductsInstock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("The product ordered is not in stock");
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
