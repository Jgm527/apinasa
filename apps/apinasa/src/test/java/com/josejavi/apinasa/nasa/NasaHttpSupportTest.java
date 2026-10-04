package com.josejavi.apinasa.nasa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URI;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;

class NasaHttpSupportTest {
    @Test
    void capturesLiveJsonAndRedactsKeyFromUrlAndEchoedLinks() {
        String privateKey = "private-test-key-42";
        URI requestUri = URI.create(
            "https://api.nasa.gov/neo/rest/v1/feed?api_key=" + privateKey
        );
        String responseJson = "{\"links\":{\"self\":\"https://api.nasa.gov/feed?api_key="
            + privateKey + "\"},\"count\":1}";

        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo(requestUri))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        ApiExchange<Map<String, Object>> exchange = NasaHttpSupport.get(
            builder.build(), requestUri, new TypeReference<>() {}, "NeoWs"
        );

        assertEquals(200, exchange.statusCode());
        assertEquals("application/json", exchange.contentType());
        assertFalse(exchange.requestUrl().contains(privateKey));
        assertFalse(exchange.responseBody().contains(privateKey));
        assertTrue(exchange.requestUrl().contains("api_key=NASA_API_KEY"));
        assertTrue(exchange.responseBody().contains("api_key=NASA_API_KEY"));
        assertEquals(1, exchange.data().get("count"));
        server.verify();
    }

    @Test
    void leavesPublicEndpointUrlWithoutInventingApiKey() {
        URI publicUri = URI.create("https://api.nasa.gov/techtransfer");

        assertEquals(publicUri.toString(), NasaHttpSupport.redactSecrets(publicUri));
    }
}