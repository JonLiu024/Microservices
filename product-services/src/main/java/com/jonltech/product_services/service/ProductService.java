package com.jonltech.product_services.service;

import com.jonltech.product_services.dto.ProductRequest;
import com.jonltech.product_services.dto.ProductResponse;
import com.jonltech.product_services.model.Product;
import com.jonltech.product_services.repository.ProductRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Data
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder().
                name(productRequest.getName()).
                description(productRequest.getDescription()).
                price(productRequest.getPrice()).build();
        productRepository.save(product);
        log.info("The product of {} is saved to product repository successfully ", product);

    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapProductToResponse).toList();

    }

    public ProductResponse mapProductToResponse(Product product) {
        return ProductResponse.builder().
                id(product.getId()).
                name(product.getName()).
                description(product.getDescription()).
                price(product.getPrice()).build();
    }


    public void deleteAProduct(List<String> products) {
        try {
            for (String id: products) {
                productRepository.deleteById(id);
                log.info("The product of {} is deleted successfully ", id);
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }
}
