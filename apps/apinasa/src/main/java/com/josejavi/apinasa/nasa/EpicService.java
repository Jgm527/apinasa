package com.josejavi.apinasa.nasa;

import com.josejavi.apinasa.nasa.NasaModels.EpicImage;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.core.type.TypeReference;

/** Consulta metadatos de imágenes EPIC usando exclusivamente el host api.nasa.gov. */
@Service
public class EpicService {
    private static final TypeReference<List<EpicImage>> EPIC_IMAGE_LIST = new TypeReference<>() {};
    private final RestClient client;
    private final String apiKey;

    public EpicService(RestClient.Builder builder, @Value("${nasa.api-key}") String apiKey) {
        this.client = builder.baseUrl("https://api.nasa.gov").build();
        this.apiKey = apiKey;
    }

    public ApiExchange<List<EpicImage>> findImages(String date) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString("https://api.nasa.gov/EPIC/api/natural");
        if (date == null || date.isBlank()) {
            uri.path("/images");
        } else {
            uri.path("/date/{date}");
        }
        uri.queryParam("api_key", apiKey);

        URI requestUri = (date == null || date.isBlank()
            ? uri.build()
            : uri.buildAndExpand(date))
            .encode()
            .toUri();
        return NasaHttpSupport.get(client, requestUri, EPIC_IMAGE_LIST, "EPIC");
    }
}