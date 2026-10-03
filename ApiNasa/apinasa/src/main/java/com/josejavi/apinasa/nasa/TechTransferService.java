package com.josejavi.apinasa.nasa;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.core.type.TypeReference;

/** Busca patentes NASA a través de la API central api.nasa.gov. */
@Service
public class TechTransferService {
    private static final TypeReference<Object> JSON_RESPONSE = new TypeReference<>() {};
    private final RestClient client;
    private final String apiKey;

    public TechTransferService(RestClient.Builder builder, @Value("${nasa.api-key}") String apiKey) {
        this.client = builder.baseUrl("https://api.nasa.gov").build();
        this.apiKey = apiKey;
    }

    public ApiExchange<Object> searchPatents(String term) {
        URI requestUri = UriComponentsBuilder.fromUriString("https://api.nasa.gov/techtransfer/patent/")
            .queryParam(term)
            .queryParam("api_key", apiKey)
            .encode()
            .build()
            .toUri();
        return NasaHttpSupport.get(client, requestUri, JSON_RESPONSE, "TechTransfer");
    }
}