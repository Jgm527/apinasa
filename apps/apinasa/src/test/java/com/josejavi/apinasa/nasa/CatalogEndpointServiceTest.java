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
    void techTransferPutsTheSearchTermInThePathWithoutAKey() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://technology.nasa.gov/api/api/patent/aircraft"))
            .andRespond(withSuccess("{\"results\":[],\"count\":0}", MediaType.APPLICATION_JSON));

        ApiExchange<Object> exchange = new TechTransferService(builder).searchPatents("aircraft");

        assertEquals(200, exchange.statusCode());
        assertEquals("https://technology.nasa.gov/api/api/patent/aircraft", exchange.requestUrl());
        assertEquals("application/json", exchange.contentType());
        server.verify();
    }

    @Test
    void techTransferEncodesSpacesAndSlashesInTheTerm() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://technology.nasa.gov/api/api/patent/solar%20panel%2Fcell"))
            .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        ApiExchange<Object> exchange = new TechTransferService(builder).searchPatents("solar panel/cell");

        assertEquals(200, exchange.statusCode());
        server.verify();
    }

    @Test
    void donkiUsesTheCcmcApiAndCamelCaseDateParametersWithoutAKey() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://ccmc.gsfc.nasa.gov/DONKI-API/get/FLR?startDate=2026-10-01&endDate=2026-10-02"))
            .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ApiExchange<?> exchange = new DonkiService(builder).findFlares("2026-10-01", "2026-10-02");

        assertEquals(200, exchange.statusCode());
        assertEquals("https://ccmc.gsfc.nasa.gov/DONKI-API/get/FLR?startDate=2026-10-01&endDate=2026-10-02",
            exchange.requestUrl());
        server.verify();
    }

    @Test
    void epicWithoutADateAsksForTheLatestImages() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://epic.gsfc.nasa.gov/api/natural"))
            .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ApiExchange<?> exchange = new EpicService(builder).findImages("");

        assertEquals(200, exchange.statusCode());
        server.verify();
    }

    @Test
    void epicDateUsesAPathSegmentOnTheEpicHost() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("https://epic.gsfc.nasa.gov/api/natural/date/2026-10-01"))
            .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ApiExchange<?> exchange = new EpicService(builder).findImages("2026-10-01");

        assertEquals(200, exchange.statusCode());
        assertEquals("https://epic.gsfc.nasa.gov/api/natural/date/2026-10-01", exchange.requestUrl());
        server.verify();
    }
}
