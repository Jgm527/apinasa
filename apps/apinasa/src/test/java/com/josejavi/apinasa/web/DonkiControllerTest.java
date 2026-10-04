package com.josejavi.apinasa.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.josejavi.apinasa.nasa.DonkiService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

class DonkiControllerTest {
    @Test
    void rejectsMoreThanThirtyDaysBeforeCallingNasa() {
        DonkiService service = mock(DonkiService.class);
        ExtendedModelMap model = new ExtendedModelMap();

        String view = new DonkiController(service).donki("2026-09-01", "2026-10-02", model);

        assertEquals("donki", view);
        assertEquals("Para la demo, limita DONKI a un máximo de 30 días por petición.", model.get("error"));
        verify(service, never()).findFlares(anyString(), anyString());
    }
}