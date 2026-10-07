package com.josejavi.apinasa.nasa;

import com.josejavi.apinasa.nasa.NasaModels.EpicImage;
import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.core.type.TypeReference;

/** Consulta metadatos de imágenes EPIC en epic.gsfc.nasa.gov. No necesita API key. */
@Service
public class EpicService {
    private static final TypeReference<List<EpicImage>> EPIC_IMAGE_LIST = new TypeReference<>() {};
    private final RestClient client;

    public EpicService(RestClient.Builder builder) {
        this.client = builder.baseUrl("https://epic.gsfc.nasa.gov").build();
    }

    public ApiExchange<List<EpicImage>> findImages(String date) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString("https://epic.gsfc.nasa.gov/api/natural");
        if (date != null && !date.isBlank()) {
            uri.path("/date/{date}");
        }

        URI requestUri = (date == null || date.isBlank() ? uri.build() : uri.buildAndExpand(date))
            .encode()
            .toUri();
        return NasaHttpSupport.get(client, requestUri, EPIC_IMAGE_LIST, "EPIC");
    }
}
