package com.josejavi.apinasa.nasa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class CatalogEndpointServiceTest {
    @Test
    void techTransferUsesApiNasaGovAndPatentQueryParameter() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://api.nasa.gov/techtransfer/patent/?aircraft&api_key=test-key"))
            .andRespond(withSuccess("{\"results\":[\"patent\",[]]}", MediaType.APPLICATION_JSON));

        ApiExchange<Object> exchange = new TechTransferService(builder, "test-key").searchPatents("aircraft");

        assertEquals(200, exchange.statusCode());
        assertEquals("https://api.nasa.gov/techtransfer/patent/?aircraft&api_key=NASA_API_KEY", exchange.requestUrl());
        assertEquals("application/json", exchange.contentType());
        server.verify();
    }

    @Test
    void donkiUsesTheCentralCatalogHostAndCamelCaseDateParameters() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://api.nasa.gov/DONKI/FLR?startDate=2026-10-01&endDate=2026-10-02&api_key=test-key"))
            .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ApiExchange<?> exchange = new DonkiService(builder, "test-key").findFlares("2026-10-01", "2026-10-02");

        assertEquals(200, exchange.statusCode());
        assertEquals("https://api.nasa.gov/DONKI/FLR?startDate=2026-10-01&endDate=2026-10-02&api_key=NASA_API_KEY",
            exchange.requestUrl());
        server.verify();
    }

    @Test
    void epicDateUsesAPathSegmentOnTheCentralCatalogHost() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://api.nasa.gov/EPIC/api/natural/date/2026-10-01?api_key=test-key"))
            .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ApiExchange<?> exchange = new EpicService(builder, "test-key").findImages("2026-10-01");

        assertEquals(200, exchange.statusCode());
        assertEquals("https://api.nasa.gov/EPIC/api/natural/date/2026-10-01?api_key=NASA_API_KEY",
            exchange.requestUrl());
        server.verify();
    }
}