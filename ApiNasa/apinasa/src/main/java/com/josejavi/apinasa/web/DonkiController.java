package com.josejavi.apinasa.web;

import com.josejavi.apinasa.nasa.ApiExchange;
import com.josejavi.apinasa.nasa.DonkiService;
import com.josejavi.apinasa.nasa.NasaModels.DonkiFlare;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DonkiController {
    private final DonkiService donkiService;

    public DonkiController(DonkiService donkiService) {
        this.donkiService = donkiService;
    }

    @GetMapping("/donki")
    public String donki(
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
            } else if (parsedEnd.isAfter(parsedStart.plusDays(29))) {
                model.addAttribute("error", "Para la demo, limita DONKI a un máximo de 30 días por petición.");
            } else {
                ApiExchange<List<DonkiFlare>> apiCall = donkiService.findFlares(startDate, endDate);
                model.addAttribute("apiCall", apiCall);
                if (apiCall.successful()) {
                    model.addAttribute("flares", apiCall.data() == null ? List.of() : apiCall.data());
                } else {
                    model.addAttribute("error", apiCall.error());
                }
            }
        } catch (DateTimeParseException exception) {
            model.addAttribute("error", "Introduce fechas válidas con formato AAAA-MM-DD.");
        }
        return "donki";
    }
}