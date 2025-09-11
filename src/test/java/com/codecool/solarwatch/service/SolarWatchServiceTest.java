package com.codecool.solarwatch.service;

import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.exception.InvalidTimezoneException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.http.HttpMethod.GET;

class SolarWatchServiceTest {

    RestTemplate http;
    MockRestServiceServer server;
    SolarWatchService service;

    // test config (fake props)
    String owBase = "https://api.openweathermap.org";
    String sunBase = "https://api.sunrise-sunset.org";
    String apiKey = "TEST_KEY";

    @BeforeEach
    void setup() {
        http = new RestTemplate();
        server = MockRestServiceServer.createServer(http);
        service = new SolarWatchService(http, owBase, apiKey, sunBase);
    }

    @Test
    void getSunTimes_happyPath_convertsToRequestedZone() {
        // 1) geocoding stub
        server.expect(once(), requestTo(org.hamcrest.Matchers.containsString(
                        owBase + "/geo/1.0/direct?q=Budapest,HU&limit=1&appid=" + apiKey)))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                    [{
                        "name":"Budapest",
                        "lat":47.4979937,
                        "lon":19.0403594,
                        "country":"HU"
                    }]
                    """, MediaType.APPLICATION_JSON));

        // 2) sunrise-sunset stub (UTC ISO strings)
        server.expect(once(), requestTo(org.hamcrest.Matchers.containsString(
                        sunBase + "/json?lat=47.4979937&lng=19.0403594&date=2025-08-23&formatted=0")))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                    {
                        "status":"OK",
                        "results":{
                        "sunrise":"2025-08-23T03:49:23+00:00",
                        "sunset":"2025-08-23T17:43:28+00:00"
                        }
                    }
                    """, MediaType.APPLICATION_JSON));

        var dto = service.getSunTimes("Budapest", "HU", null,
                LocalDate.parse("2025-08-23"), "Europe/Budapest");

        assertThat(dto.city()).isEqualTo("Budapest");
        assertThat(dto.coordinates().lat()).isEqualTo(47.4979937);
        assertThat(dto.timezone()).isEqualTo("Europe/Budapest");
        // offset must be +02:00 in Aug
        assertThat(OffsetDateTime.parse(dto.sunrise().toString()).getOffset().getTotalSeconds()).isEqualTo(2*3600);
        server.verify();
    }

    @Test
    void getSunTimes_cityNotFound_throws404() {
        server.expect(once(), requestTo(org.hamcrest.Matchers.containsString(
                        owBase + "/geo/1.0/direct?q=Nowhere,HU&limit=1&appid=" + apiKey)))
                .andExpect(method(GET))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        assertThrows(CityNotFoundException.class, () ->
                service.getSunTimes("Nowhere", "HU", null, LocalDate.now(), "UTC"));
        server.verify();
    }

    /*@Test
    void getSunTimes_invalidTimezone_throws400() {
        // geocode still called first
        server.expect(once(), requestTo(org.hamcrest.Matchers.containsString(
                        owBase + "/geo/1.0/direct?q=Budapest,HU&limit=1&appid=" + apiKey)))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                    [{
                        "name":"Budapest","lat":47.4979,"lon":19.0402,"country":"HU"
                    }]
                    """, MediaType.APPLICATION_JSON));

        // invalid tz should fail before sunrise API call if you parse early; if you parse later,
        // you can still keep this test to assert it eventually throws.
        assertThrows(com.codecool.solarwatch.exception.InvalidTimezoneException.class, () ->
                service.getSunTimes("Budapest", "HU", null, LocalDate.now(), "Europe/Buda pest "));
        server.verify();
    }*/

    @Test
    void getSunTimes_invalidTimezone_throws400() {
        assertThrows(InvalidTimezoneException.class, () ->
                service.getSunTimes("Budapest", "HU", null, LocalDate.now(), "Europe/Buda pest ")
        );
        server.verify(); // optional here, since we expect 0 calls
    }
}
