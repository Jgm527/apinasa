package com.josejavi.ejemplo.mvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.client.RestClient;

import com.josejavi.ejemplo.mvc.web.NasaController;

/**
 * Tests que NO llaman a la NASA real:
 *   - Se instancia el controlador directamente (como cualquier clase).
 *   - Las respuestas de NASA se simulan con MockRestServiceServer:
 *     un servidor HTTP de mentira que devuelve JSON preparado a mano.
 *     Es la misma técnica que usa el proyecto ApiNasa/apinasa.
 */
class EjemploMvcApplicationTests {

    @Test
    void laRaizRedirigeAEpic() {
        NasaController controlador =
                new NasaController(RestClient.builder(), "DEMO_KEY");

        assertEquals("redirect:/epic", controlador.index());
    }

    @Test
    void epicLlamaANasaYPasaLosDatosALaPlantilla() {
        // 1. Crear un cliente atado a un servidor HTTP simulado
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();

        // 2. Preparar la respuesta que "NASA" devolverá
        String json = """
                [
                  {
                    "identifier": "epic_1b_20261001",
                    "caption": "Earth imagery captured by EPIC",
                    "date": "2026-10-01 12:00:00",
                    "centroid_coordinates": { "lat": -12.5, "lon": 45.2 }
                  }
                ]
                """;
        server.expect(requestTo(
                "https://api.nasa.gov/EPIC/api/natural/images?api_key=DEMO_KEY"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        // 3. Ejecutar el método del controlador, con un Model de verdad
        ExtendedModelMap model = new ExtendedModelMap();
        NasaController controlador = new NasaController(builder, "DEMO_KEY");
        String vista = controlador.epic(model);

        // 4. Comprobar la vista, los datos del Model y la URL mostrada
        server.verify();
        assertEquals("epic", vista);

        @SuppressWarnings("unchecked")
        List<NasaController.EpicImage> images =
                (List<NasaController.EpicImage>) model.get("images");
        assertEquals(1, images.size());
        assertEquals("epic_1b_20261001", images.getFirst().identifier());

        // La clave nunca llega a la plantilla: se redacta antes
        assertEquals(
                "https://api.nasa.gov/EPIC/api/natural/images?api_key=NASA_API_KEY",
                model.get("requestUrl"));
    }

    @Test
    void laAplicacionArranca() {
        // Si el contexto de Spring arranca, este test ya ha pasado.
        // Es la comprobación mínima de que la aplicación está bien configurada.
    }
}
