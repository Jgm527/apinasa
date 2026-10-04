package com.josejavi.apinasa.nasa;

import com.josejavi.apinasa.nasa.NasaModels.NeoFeed;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.core.type.TypeReference;

/** Cliente de NeoWs. El intervalo de siete días se valida en la capa web. */
@Service
public class NeoWsService {
    private static final TypeReference<NeoFeed> NEO_FEED = new TypeReference<>() {};
    private final RestClient client;
    private final String apiKey;

    public NeoWsService(RestClient.Builder builder, @Value("${nasa.api-key}") String apiKey) {
        this.client = builder.baseUrl("https://api.nasa.gov").build();
        this.apiKey = apiKey;
    }

    public ApiExchange<NeoFeed> findApproaches(String startDate, String endDate) {
        URI requestUri = UriComponentsBuilder.fromUriString("https://api.nasa.gov/neo/rest/v1/feed")
            .queryParam("start_date", startDate)
            .queryParam("end_date", endDate)
            .queryParam("api_key", apiKey)
            .encode()
            .build()
            .toUri();
        return NasaHttpSupport.get(client, requestUri, NEO_FEED, "NeoWs");
    }
}