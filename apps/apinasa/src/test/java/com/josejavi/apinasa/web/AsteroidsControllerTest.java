package com.josejavi.apinasa.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.josejavi.apinasa.nasa.NeoWsService;
import com.josejavi.apinasa.nasa.ApiExchange;
import com.josejavi.apinasa.nasa.NasaModels.NeoFeed;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

class AsteroidsControllerTest {
    @Test
    void defaultsToSevenDaysEndingToday() {
        NeoWsService service = mock(NeoWsService.class);
        when(service.findApproaches(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString()))
            .thenReturn(new ApiExchange<>(
                "GET", "https://api.nasa.gov/neo/rest/v1/feed?api_key=NASA_API_KEY", 200,
                "application/json", "{}", new NeoFeed(Map.of()), null
            ));
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        ExtendedModelMap model = new ExtendedModelMap();

        String view = new AsteroidsController(service).asteroids(null, null, model);

        verify(service).findApproaches(today.minusDays(6).toString(), today.toString());
        assertEquals("asteroids", view);
        assertEquals(java.util.List.of(), model.get("asteroids"));
    }

    @Test
    void rejectsRangesLongerThanSevenDaysBeforeCallingNasa() {
        NeoWsService service = mock(NeoWsService.class);
        ExtendedModelMap model = new ExtendedModelMap();

        new AsteroidsController(service).asteroids("2026-09-01", "2026-09-08", model);

        verify(service, never()).findApproaches(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString());
        assertEquals("NeoWs permite consultar como máximo siete días por petición.", model.get("error"));
    }
}