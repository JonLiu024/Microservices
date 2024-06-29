package com.jonltech.product_services.controller;

import com.jonltech.product_services.dto.ProductRequest;
import com.jonltech.product_services.dto.ProductResponse;
import com.jonltech.product_services.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@RequestParam(name = "id") List<String> products) {
        productService.deleteAProduct(products);

    }

}
