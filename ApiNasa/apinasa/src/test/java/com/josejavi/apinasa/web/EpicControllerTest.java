package com.josejavi.apinasa.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.josejavi.apinasa.nasa.EpicService;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

class EpicControllerTest {
    @Test
    void rejectsFutureDateBeforeCallingNasa() {
        EpicService service = mock(EpicService.class);
        ExtendedModelMap model = new ExtendedModelMap();
        String futureDate = LocalDate.now(ZoneOffset.UTC).plusDays(1).toString();

        String view = new EpicController(service).epic(futureDate, model);

        assertEquals("epic", view);
        assertEquals("Elige una fecha que no sea futura.", model.get("error"));
        verify(service, never()).findImages(anyString());
    }
}