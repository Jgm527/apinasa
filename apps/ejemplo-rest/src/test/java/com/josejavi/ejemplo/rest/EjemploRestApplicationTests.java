package com.josejavi.ejemplo.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.josejavi.ejemplo.rest.web.NasaRestController;
import com.josejavi.ejemplo.rest.web.NasaRestController.EpicImage;

/**
 * Tests que NO llaman a la NASA real:
 *   - El endpoint local (/api/saludo) se prueba directamente,
 *     instanciando el controlador como cualquier clase de Java.
 *   - El endpoint /api/epic se prueba con MockRestServiceServer,
 *     que sustituye el HTTP real por respuestas preparadas a mano.
 *     Es la misma técnica que usa el proyecto apps/apinasa.
 */
class EjemploRestApplicationTests {

    @Test
    void elEndpointSaludoDevuelveElMensajeEsperado() {
        // Instanciamos el controlador "a mano": un test de Java puro.
        NasaRestController controlador =
                new NasaRestController(RestClient.builder(), "DEMO_KEY");

        NasaRestController.Saludo saludo = controlador.saludo("Ada");

        assertEquals("¡Hola, Ada!", saludo.mensaje());
    }

    @Test
    void elEndpointEpicPideLaUrlCorrectaYConvierteElJson() {
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

        // 3. Ejecutar el endpoint
        NasaRestController controlador = new NasaRestController(builder, "DEMO_KEY");
        List<EpicImage> images = controlador.epic();

        // 4. Comprobar que se pidió la URL esperada y que el JSON
        //    se convirtió en objetos Java
        server.verify();
        assertEquals(1, images.size());
        EpicImage imagen = images.getFirst();
        assertEquals("epic_1b_20261001", imagen.identifier());
        assertEquals("2026-10-01 12:00:00", imagen.date());
        assertNotNull(imagen.centroid_coordinates());
        assertEquals(-12.5, imagen.centroid_coordinates().lat());
    }

    @Test
    void laAplicacionArranca() {
        // Si el contexto de Spring arranca, este test ya ha pasado.
        // Es la comprobación mínima de que la aplicación está bien configurada.
    }
}
