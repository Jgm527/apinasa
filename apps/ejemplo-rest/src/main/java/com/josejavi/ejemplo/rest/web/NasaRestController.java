package com.josejavi.ejemplo.rest.web;

import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

/**
 * CONTROLADOR REST.
 *
 * Diferencia clave con {@code @Controller}:
 *   - {@code @RestController} devuelve DATOS. Cada método devuelve un objeto Java
 *     y Spring lo convierte automáticamente a JSON (gracias a Jackson).
 *   - {@code @Controller} devuelve el NOMBRE de una plantilla HTML (ver proyecto ejemplo-mvc).
 *
 * Las rutas de la API se declaran completas en cada {@code @GetMapping}.
 */
@RestController
public class NasaRestController {

    private final RestClient restClient;

    /**
     * Constructor: Spring inyecta las dependencias automáticamente.
     *
     * @param builder  RestClient.Builder lo crea Spring; lo usamos para construir el
     *                 cliente HTTP con el que se llamará a otras APIs.
     */
    public NasaRestController(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    @GetMapping("/")
    public Saludo inicio() {
        return new Saludo("API REST activa. Prueba /api/saludo?nombre=Ada o /api/epic.");
    }

    /**
     * Endpoint 1: ejemplo que NO llama a ninguna API externa.
     *
     * Prueba: http://localhost:8080/api/saludo?nombre=Ada
     *
     * {@code @GetMapping} = responde a peticiones GET de esta ruta.
     * {@code @RequestParam} = lee un query parameter (?nombre=Ada);
     *                         defaultValue evita errores si no viene.
     * El objeto Saludo se serializa solo a JSON: {"mensaje":"¡Hola, Ada!"}
     */
    @GetMapping("/api/saludo")
    public Saludo saludo(@RequestParam(defaultValue = "mundo") String nombre) {
        return new Saludo("¡Hola, " + nombre + "!");
    }

    /**
     * Endpoint 2: consume la API real de NASA (EPIC: metadatos de imágenes de la Tierra).
     *
     * Prueba: http://localhost:8080/api/epic   (necesita Internet)
     *
     * El flujo de toda llamada a una API externa es siempre el mismo:
     *   1. Construir la URL de destino.
     *   2. Hacer la petición HTTP.
     *   3. Convertir la respuesta JSON en objetos Java (DTOs).
     */
    @GetMapping("/api/epic")
    public List<EpicImage> epic() {

        // 1) y 2) Petición GET a la API EPIC v2 y conversión a objetos Java.
        //    - .accept(...) = cabecera Accept: le decimos a NASA que queremos JSON.
        //    - .retrieve()  = ejecuta la petición.
        //    - .body(...)   = convierte el JSON de respuesta al tipo indicado.
        //
        //    Como la respuesta es una LISTA, hay que usar ParameterizedTypeReference
        //    (Java "olvida" el tipo genérico en tiempo de ejecución).
        //    Para un objeto simple bastaría con: .body(EpicImage.class)
        return restClient.get()
                .uri("https://epic.gsfc.nasa.gov/api/natural")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<List<EpicImage>>() {});
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, Object>> nasaError(RestClientResponseException exception) {
        int status = exception.getStatusCode().value();
        String message = status == 429
                ? "La API de NASA ha limitado las solicitudes. Inténtalo más tarde."
                : "La API de NASA respondió con un error.";

        return ResponseEntity.status(exception.getStatusCode())
                .body(Map.of("status", status, "error", message));
    }

    // -----------------------------------------------------------------
    // DTOs: clases que representan la estructura del JSON de la API.
    // Un "record" es una clase inmutable de datos (Java 16+): muy compacta.
    // Los nombres de los campos deben coincidir con las claves del JSON.
    // -----------------------------------------------------------------

    /** Respuesta del endpoint local. Se convierte a {"mensaje": "..."} */
    public record Saludo(String mensaje) {}

    /**
     * Una imagen EPIC. Solo declaramos los campos que nos interesan;
     * el resto del JSON de NASA se ignora sin error.
     */
    public record EpicImage(
            String identifier,
            String image,
            String caption,
            CentroidCoordinates centroid_coordinates) {

        /** Objeto anidado del JSON: "centroid_coordinates": { "lat": ..., "lon": ... } */
        public record CentroidCoordinates(Double lat, Double lon) {}
    }
}
