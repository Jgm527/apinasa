package com.josejavi.apinasa.nasa;

import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.core.type.TypeReference;

/** Busca patentes NASA en technology.nasa.gov. No necesita API key. */
@Service
public class TechTransferService {
    private static final TypeReference<Object> JSON_RESPONSE = new TypeReference<>() {};
    private final RestClient client;

    public TechTransferService(RestClient.Builder builder) {
        this.client = builder.baseUrl("https://technology.nasa.gov").build();
    }

    public ApiExchange<Object> searchPatents(String term) {
        URI requestUri = UriComponentsBuilder.fromUriString("https://technology.nasa.gov/api/api/patent/{term}")
            .encode()
            .buildAndExpand(term)
            .toUri();
        return NasaHttpSupport.get(client, requestUri, JSON_RESPONSE, "TechTransfer");
    }
}
