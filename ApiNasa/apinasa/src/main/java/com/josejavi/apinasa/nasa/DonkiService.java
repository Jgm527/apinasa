package com.josejavi.apinasa.nasa;

import com.josejavi.apinasa.nasa.NasaModels.DonkiFlare;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.core.type.TypeReference;

/** Consulta fulguraciones solares desde la entrada DONKI del portal api.nasa.gov. */
@Service
public class DonkiService {
    private static final TypeReference<List<DonkiFlare>> FLARE_LIST = new TypeReference<>() {};
    private final RestClient client;
    private final String apiKey;

    public DonkiService(RestClient.Builder builder, @Value("${nasa.api-key}") String apiKey) {
        this.client = builder.baseUrl("https://api.nasa.gov").build();
        this.apiKey = apiKey;
    }

    public ApiExchange<List<DonkiFlare>> findFlares(String startDate, String endDate) {
        URI requestUri = UriComponentsBuilder.fromUriString("https://api.nasa.gov/DONKI/FLR")
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .queryParam("api_key", apiKey)
            .encode()
            .build()
            .toUri();
        return NasaHttpSupport.get(client, requestUri, FLARE_LIST, "DONKI FLR");
    }
}