package com.josejavi.apinasa.web;

import com.josejavi.apinasa.nasa.ApiExchange;
import com.josejavi.apinasa.nasa.EpicService;
import com.josejavi.apinasa.nasa.NasaModels.EpicImage;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EpicController {
    private final EpicService epicService;

    public EpicController(EpicService epicService) {
        this.epicService = epicService;
    }

    @GetMapping("/epic")
    public String epic(@RequestParam(required = false) String date, Model model) {
        String selectedDate = date == null ? "" : date;
        model.addAttribute("date", selectedDate);
        model.addAttribute("today", LocalDate.now(ZoneOffset.UTC));

        try {
            if (!selectedDate.isBlank() && LocalDate.parse(selectedDate).isAfter(LocalDate.now(ZoneOffset.UTC))) {
                model.addAttribute("error", "Elige una fecha que no sea futura.");
            } else {
                ApiExchange<List<EpicImage>> apiCall = epicService.findImages(selectedDate);
                model.addAttribute("apiCall", apiCall);
                if (apiCall.successful()) {
                    model.addAttribute("images", apiCall.data() == null ? List.of() : apiCall.data());
                } else {
                    model.addAttribute("error", apiCall.error());
                }
            }
        } catch (DateTimeParseException exception) {
            model.addAttribute("error", "La fecha debe tener formato AAAA-MM-DD.");
        }
        return "epic";
    }
}