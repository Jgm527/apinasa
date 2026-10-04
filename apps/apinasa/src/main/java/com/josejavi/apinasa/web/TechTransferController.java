package com.josejavi.apinasa.web;

import com.josejavi.apinasa.nasa.ApiExchange;
import com.josejavi.apinasa.nasa.TechTransferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TechTransferController {
    private final TechTransferService techTransferService;

    public TechTransferController(TechTransferService techTransferService) {
        this.techTransferService = techTransferService;
    }

    @GetMapping("/techtransfer")
    public String searchPatents(@RequestParam(defaultValue = "aircraft") String q, Model model) {
        String term = q.trim();
        model.addAttribute("q", term);

        if (term.isBlank() || term.length() > 80) {
            model.addAttribute("error", "Escribe un término de búsqueda de entre 1 y 80 caracteres.");
        } else {
            ApiExchange<Object> apiCall = techTransferService.searchPatents(term);
            model.addAttribute("apiCall", apiCall);
            if (!apiCall.successful()) {
                model.addAttribute("error", apiCall.error());
            }
        }
        return "techtransfer";
    }
}