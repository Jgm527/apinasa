package com.josejavi.ejemplo.mvc.web;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * CONTROLADOR MVC (clásico, como el proyecto ApiNasa).
 *
 * Diferencia clave con {@code @RestController}:
 *   - {@code @Controller} devuelve el NOMBRE de una plantilla Thymeleaf (un HTML).
 *   - Los datos van en el objeto {@code Model}: es el "maletín" que el
 *     controlador le pasa a la plantilla.
 *   - El navegador recibe HTML renderizado, no JSON.
 */
@Controller
public class NasaController {

    private final RestClient restClient;
    private final String apiKey;

    /**
     * Constructor: Spring inyecta las dependencias automáticamente.
     *
     * @param builder RestClient.Builder lo crea Spring; lo usamos para construir
     *                el cliente HTTP con el que se llamará a otras APIs.
     * @param apiKey  {@code @Value} lee la propiedad "nasa.api-key" de
     *                application.properties. Sin configuración extra, vale "DEMO_KEY".
     */
    public NasaController(RestClient.Builder builder,
                            @Value("${nasa.api-key}") String apiKey) {
        this.restClient = builder.build();
        this.apiKey = apiKey;
    }

    /**
     * Ruta raíz: redirige a /epic.
     * Devolver "redirect:/epic" indica a Spring que responda con una redirección.
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/epic";
    }

    /**
     * Endpoint que consume la API real de NASA (EPIC: metadatos de imágenes
     * de la Tierra) y muestra los resultados en una página HTML.
     *
     * Prueba: http://localhost:8080/epic   (necesita Internet)
     *
     * Flujo MVC en 3 pasos:
     *   1. El controlador llama a la API externa (RestClient).
     *   2. Guarda los datos en el Model.
     *   3. Devuelve el nombre de la plantilla ("epic" = templates/epic.html).
     */
    @GetMapping("/epic")
    public String epic(Model model) {

        // 1) Construir la URL de destino. UriComponentsBuilder codifica los
        //    parámetros correctamente, no se escribe la URL "a mano".
        URI url = UriComponentsBuilder
                .fromUriString("https://api.nasa.gov/EPIC/api/natural/images")
                .queryParam("api_key", apiKey)
                .encode()
                .build()
                .toUri();

        // 2) Petición GET y conversión del JSON a objetos Java (DTOs).
        //    ParameterizedTypeReference es necesario por ser una LISTa.
        List<EpicImage> images = restClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON) // cabecera Accept: queremos JSON
                .retrieve()
                .body(new ParameterizedTypeReference<List<EpicImage>>() {});

        // 3) Datos para la plantilla: el Model es el "maletín" compartido.
        model.addAttribute("images", images);

        // Mostramos la URL real de la petición (didáctica), pero NUNCA la
        // clave: la sustituimos por un texto seguro antes de enviarla a la vista.
        model.addAttribute("requestUrl",
                url.toString().replace(apiKey, "NASA_API_KEY"));

        // El nombre de la plantilla (sin extensión): src/main/resources/templates/epic.html
        return "epic";
    }

    // -----------------------------------------------------------------
    // DTOs: clases que representan la estructura del JSON de la API.
    // Un "record" es una clase inmutable de datos (Java 16+).
    // Los nombres de los campos deben coincidir con las claves del JSON.
    // -----------------------------------------------------------------

    /**
     * Una imagen EPIC. Solo declaramos los campos que usamos en la vista;
     * el resto del JSON de NASA se ignora sin error.
     */
    public record EpicImage(
            String identifier,
            String caption,
            String date,
            CentroidCoordinates centroid_coordinates) {

        /** Objeto anidado del JSON: "centroid_coordinates": { "lat": ..., "lon": ... } */
        public record CentroidCoordinates(Double lat, Double lon) {}
    }
}
