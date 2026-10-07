package com.josejavi.apinasa.nasa;

import com.josejavi.apinasa.nasa.NasaModels.DonkiFlare;
import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.core.type.TypeReference;

/** Consulta fulguraciones solares (FLR) en la API pública de DONKI. No necesita API key. */
@Service
public class DonkiService {
    private static final TypeReference<List<DonkiFlare>> FLARE_LIST = new TypeReference<>() {};
    private final RestClient client;

    public DonkiService(RestClient.Builder builder) {
        this.client = builder.baseUrl("https://ccmc.gsfc.nasa.gov").build();
    }

    public ApiExchange<List<DonkiFlare>> findFlares(String startDate, String endDate) {
        URI requestUri = UriComponentsBuilder.fromUriString("https://ccmc.gsfc.nasa.gov/DONKI-API/get/FLR")
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .encode()
            .build()
            .toUri();
        return NasaHttpSupport.get(client, requestUri, FLARE_LIST, "DONKI FLR");
    }
}
