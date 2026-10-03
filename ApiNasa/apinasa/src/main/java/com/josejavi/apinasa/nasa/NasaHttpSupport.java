package com.josejavi.apinasa.nasa;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

/** Lee una vez el cuerpo, lo conserva para la vista y parsea esos mismos datos. */
public final class NasaHttpSupport {
    private static final Logger logger = LoggerFactory.getLogger(NasaHttpSupport.class);
    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();

    private NasaHttpSupport() {}

    public static <T> ApiExchange<T> get(
        RestClient client,
        URI requestUri,
        TypeReference<T> responseType,
        String serviceName
    ) {
        String safeUrl = redactSecrets(requestUri);
        try {
            ResponseEntity<String> response = client.get()
                .uri(requestUri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);
            return createExchange(
                safeUrl,
                response.getStatusCode().value(),
                contentType(response.getHeaders().getContentType()),
                redactResponseSecrets(requestUri, response.getBody()),
                responseType,
                serviceName
            );
        } catch (RestClientResponseException exception) {
            return createExchange(
                safeUrl,
                exception.getStatusCode().value(),
                contentType(exception.getResponseHeaders() == null
                    ? null
                    : exception.getResponseHeaders().getContentType()),
                redactResponseSecrets(requestUri, exception.getResponseBodyAsString()),
                responseType,
                serviceName
            );
        } catch (RestClientException exception) {
            logger.warn("{} request failed: {}", serviceName, exception.getClass().getSimpleName());
            return new ApiExchange<>(
                "GET", safeUrl, 0, "Sin respuesta", "", null,
                "No hubo respuesta HTTP de " + serviceName + " (" + exception.getClass().getSimpleName() + ")."
            );
        }
    }

    private static <T> ApiExchange<T> createExchange(
        String requestUrl,
        int statusCode,
        String contentType,
        String responseBody,
        TypeReference<T> responseType,
        String serviceName
    ) {
        String body = responseBody == null ? "" : responseBody;
        T data = null;
        String error = null;

        if (statusCode >= 200 && statusCode < 300 && !body.isBlank()) {
            try {
                data = JSON_MAPPER.readValue(body, responseType);
            } catch (JacksonException exception) {
                logger.warn("{} response is not valid JSON: {}", serviceName, exception.getClass().getSimpleName());
                error = "La respuesta HTTP llegó, pero no se pudo interpretar como JSON.";
            }
        } else if (statusCode >= 200 && statusCode < 300) {
            error = "El servicio respondió correctamente, pero el cuerpo está vacío.";
        } else if (statusCode == 429) {
            error = serviceName.equals("NeoWs")
                ? "NASA agotó la cuota de NeoWs. Configura tu NASA_API_KEY personal o espera a que se renueve la cuota."
                : serviceName + " limitó temporalmente las peticiones. Espera antes de volver a consultar.";
        } else {
            error = serviceName + " respondió con HTTP " + statusCode + ".";
        }

        return new ApiExchange<>("GET", requestUrl, statusCode, contentType, body, data, error);
    }

    static String redactSecrets(URI requestUri) {
        String rawQuery = requestUri.getRawQuery();
        if (rawQuery == null || java.util.Arrays.stream(rawQuery.split("&"))
            .noneMatch(parameter -> parameter.equals("api_key") || parameter.startsWith("api_key="))) {
            return requestUri.toString();
        }
        return UriComponentsBuilder.fromUri(requestUri)
            .replaceQueryParam("api_key", "NASA_API_KEY")
            .build(true)
            .toUriString();
    }

    private static String redactResponseSecrets(URI requestUri, String responseBody) {
        if (responseBody == null || responseBody.isEmpty() || requestUri.getRawQuery() == null) {
            return responseBody;
        }

        String safeBody = responseBody;
        for (String parameter : requestUri.getRawQuery().split("&")) {
            int separator = parameter.indexOf('=');
            if (separator < 0 || !parameter.substring(0, separator).equals("api_key")) {
                continue;
            }

            String encodedSecret = parameter.substring(separator + 1);
            String secret = URLDecoder.decode(encodedSecret, StandardCharsets.UTF_8);
            if (!secret.isEmpty()) {
                safeBody = safeBody.replace(secret, "NASA_API_KEY");
                safeBody = safeBody.replace(encodedSecret, "NASA_API_KEY");
            }
        }
        return safeBody;
    }

    private static String contentType(MediaType contentType) {
        return contentType == null ? "Desconocido" : contentType.toString();
    }
}