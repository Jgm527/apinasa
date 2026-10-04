package com.josejavi.apinasa.web;

import com.josejavi.apinasa.nasa.ApiExchange;
import com.josejavi.apinasa.nasa.NeoWsService;
import com.josejavi.apinasa.nasa.NasaModels.CloseApproach;
import com.josejavi.apinasa.nasa.NasaModels.NeoFeed;
import com.josejavi.apinasa.nasa.NasaModels.NeoObject;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AsteroidsController {
    private final NeoWsService neoWsService;

    public AsteroidsController(NeoWsService neoWsService) {
        this.neoWsService = neoWsService;
    }

    @GetMapping("/asteroids")
    public String asteroids(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        Model model
    ) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        String startDate = start == null || start.isBlank() ? today.minusDays(6).toString() : start;
        String endDate = end == null || end.isBlank() ? today.toString() : end;
        model.addAttribute("start", startDate);
        model.addAttribute("end", endDate);
        model.addAttribute("today", today);

        try {
            LocalDate parsedStart = LocalDate.parse(startDate);
            LocalDate parsedEnd = LocalDate.parse(endDate);
            if (parsedStart.isAfter(parsedEnd)) {
                model.addAttribute("error", "La fecha inicial debe ser anterior o igual a la fecha final.");
            } else if (parsedEnd.isAfter(today)) {
                model.addAttribute("error", "El rango no puede incluir fechas futuras.");
            } else if (parsedEnd.isAfter(parsedStart.plusDays(6))) {
                model.addAttribute("error", "NeoWs permite consultar como máximo siete días por petición.");
            } else {
                ApiExchange<NeoFeed> apiCall = neoWsService.findApproaches(startDate, endDate);
                model.addAttribute("apiCall", apiCall);
                if (!apiCall.successful()) {
                    model.addAttribute("error", apiCall.error());
                } else {
                    List<AsteroidRow> rows = new ArrayList<>();
                    NeoFeed feed = apiCall.data();
                    if (feed != null && feed.near_earth_objects() != null) {
                        feed.near_earth_objects().forEach((date, objects) -> objects.forEach(asteroid ->
                            rows.add(toRow(date, asteroid))));
                    }
                    model.addAttribute("asteroids", rows);
                }
            }
        } catch (DateTimeParseException exception) {
            model.addAttribute("error", "Introduce fechas válidas con formato AAAA-MM-DD.");
        }
        return "asteroids";
    }

    private AsteroidRow toRow(String date, NeoObject asteroid) {
        CloseApproach approach = asteroid.close_approach_data() == null || asteroid.close_approach_data().isEmpty()
            ? null
            : asteroid.close_approach_data().getFirst();
        String distanceKm = approach == null || approach.miss_distance() == null
            ? "Sin dato"
            : approach.miss_distance().getOrDefault("kilometers", "Sin dato");
        return new AsteroidRow(
            date,
            asteroid.name(),
            asteroid.is_potentially_hazardous_asteroid(),
            distanceKm
        );
    }

    public record AsteroidRow(String date, String name, boolean potentiallyHazardous, String distanceKm) {}
}