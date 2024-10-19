package com.jonltech.wildlife_info_services.repository;

import com.jonltech.wildlife_info_services.model.Wildlife;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WildlifeRepository extends MongoRepository<Wildlife, String> {

}
