package com.jonltech.wildlife_info_services.service;

import com.jonltech.wildlife_info_services.dto.WildlifeRequest;
import com.jonltech.wildlife_info_services.dto.WildlifeResponse;
import com.jonltech.wildlife_info_services.event.WildlifeProfileCreatedEvent;
import com.jonltech.wildlife_info_services.model.Wildlife;
import com.jonltech.wildlife_info_services.repository.WildlifeRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Data
@RequiredArgsConstructor
@Service
public class WildlifeService {


    private final WildlifeRepository wildlifeRepository;
    private final KafkaTemplate<String, WildlifeProfileCreatedEvent> kafkaTemplate;

    public void createProduct(WildlifeRequest wildlifeRequest) {
        Wildlife product = Wildlife.builder().
                nickName(wildlifeRequest.getName()).
                species(wildlifeRequest.getSpecies()).
                conservationStatus(wildlifeRequest.getConservationStatus()).
                description(wildlifeRequest.getDescription()).
                fundingGoal(wildlifeRequest.getPrice()).build();
        wildlifeRepository.save(product);
        kafkaTemplate.send("notificationTopic", new WildlifeProfileCreatedEvent(wildlifeRequest.getId()));


    }

    public List<WildlifeResponse> getAllWildlifeProfiles() {
        List<Wildlife> wildlife = wildlifeRepository.findAll();
        return wildlife.stream().map(this::mapWildlifeToResponse).toList();

    }

    public WildlifeResponse mapWildlifeToResponse(Wildlife wildlife) {
        return WildlifeResponse.builder().
                id(wildlife.getId()).
                nickName(wildlife.getNickName()).
                conservationStatus(wildlife.getConservationStatus()).
                description(wildlife.getDescription()).
                fundingGoal(wildlife.getFundingGoal()).build();
    }


    public void deleteAProduct(List<String> products) {
        try {
            for (String id: products) {
                wildlifeRepository.deleteById(id);

            }
        } catch (Exception e) {
            e.getMessage();
        }

    }
}
