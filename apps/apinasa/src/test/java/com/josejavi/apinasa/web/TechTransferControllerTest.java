package com.josejavi.apinasa.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.josejavi.apinasa.nasa.TechTransferService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

class TechTransferControllerTest {
    @Test
    void rejectsBlankSearchWithoutCallingNasa() {
        TechTransferService service = mock(TechTransferService.class);
        ExtendedModelMap model = new ExtendedModelMap();

        String view = new TechTransferController(service).searchPatents("   ", model);

        assertEquals("techtransfer", view);
        assertEquals("Escribe un término de búsqueda de entre 1 y 80 caracteres.", model.get("error"));
        verify(service, never()).searchPatents(anyString());
    }
}