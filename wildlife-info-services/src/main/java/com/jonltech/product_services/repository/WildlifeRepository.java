package com.jonltech.product_services.repository;

import com.jonltech.product_services.model.Wildlife;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WildlifeRepository extends MongoRepository<Wildlife, String> {

}
