package com.service;

import dto.DemandForecast;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DemandForecastService {

    List<DemandForecast> getDemandForecastsByProductId(UUID productId);

    DemandForecast createDemandForecast(UUID productId, DemandForecast demandForecast);

    DemandForecast updateDemandForecast(UUID productId, UUID forecastId, DemandForecast demandForecast);

    void deleteDemandForecastAndValidateByProductId(UUID productId, UUID forecastId);

    void deleteDemandForecast(UUID productId);

    void deleteAll();
}
