package com.jonltech.wildlife_info_services.controller;

import com.jonltech.wildlife_info_services.dto.WildlifeRequest;
import com.jonltech.wildlife_info_services.dto.WildlifeResponse;
import com.jonltech.wildlife_info_services.service.WildlifeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wildlife")
@RequiredArgsConstructor
public class WildlifeController {

    private final WildlifeService wildlifeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createWl(@RequestBody WildlifeRequest wildlifeRequest) {
        wildlifeService.createProduct(wildlifeRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WildlifeResponse> getAllWl() {
        return wildlifeService.getAllWildlifeProfiles();
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@RequestParam(name = "id") List<String> products) {
        wildlifeService.deleteAProduct(products);

    }

}
