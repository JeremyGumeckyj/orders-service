package com.service.controller;

import com.service.DemandForecastService;
import dto.DemandForecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class DemandForecastController {

    @Autowired
    DemandForecastService demandForecastService;

    @GetMapping("/demandForecast/{productId}")
    public List<DemandForecast> getDemandForecastsByProductId(@PathVariable UUID productId) {
        return demandForecastService.getDemandForecastsByProductId(productId);
    }

    @PostMapping("/demandForecast/{productId}")
    public DemandForecast createDemandForecast(@PathVariable UUID productId, @RequestBody DemandForecast demandForecast) {
        return demandForecastService.createDemandForecast(productId, demandForecast);
    }

    @PutMapping("/demandForecast/{productId}/{forecastId}")
    public DemandForecast updateDemandForecast(@PathVariable UUID productId, @PathVariable UUID forecastId, @RequestBody DemandForecast demandForecast) {
        return demandForecastService.updateDemandForecast(productId, forecastId, demandForecast);
    }

    @DeleteMapping("/demandForecast/{id}")
    public ResponseEntity deleteDemandForecast(@PathVariable UUID id) {
        demandForecastService.deleteDemandForecast(id);
        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/demandForecast/deleteAll")
    public void deleteAll() {
        demandForecastService.deleteAll();
    }

//    @DeleteMapping
//    public void deleteDemandForecast(@PathVariable UUID productId, @PathVariable UUID forecastId) {
//        demandForecastService.deleteDemandForecast(productId, forecastId);
//        return ResponseEntity.ok()
//                .build();
//    }
}