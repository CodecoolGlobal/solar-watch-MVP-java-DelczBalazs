package com.codecool.solarwatch.client;

import com.codecool.solarwatch.dto.openweather.OpenWeatherDirectItemDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenWeatherClientTest {

    @Test
    void buildsEncodedUri_andReturnsFirstItem() {
        // Arrange
        RestTemplate http = mock(RestTemplate.class);
        String base = "https://api.openweathermap.org";
        String apiKey = "KEY123";
        OpenWeatherClient client = new OpenWeatherClient(http, base, apiKey);

        OpenWeatherDirectItemDto[] payload = new OpenWeatherDirectItemDto[] {
                new OpenWeatherDirectItemDto("Buenos Aires", -34.6037, -58.3816, "AR", "Buenos Aires"),
                new OpenWeatherDirectItemDto("Buenos Aires Province", -35.0, -59.0, "AR", "Buenos Aires")
        };

        ArgumentCaptor<URI> uriCap = ArgumentCaptor.forClass(URI.class);
        when(http.getForObject(uriCap.capture(), eq(OpenWeatherDirectItemDto[].class))).thenReturn(payload);

        // Act
        OpenWeatherDirectItemDto out = client.fetchFirstMatch("Buenos Aires,AR");

        // Assert: first item returned
        assertThat(out).isNotNull();
        assertThat(out.name()).isEqualTo("Buenos Aires");
        assertThat(out.country()).isEqualTo("AR");

        // Assert URI parts
        URI built = uriCap.getValue();
        assertThat(built.getHost()).isEqualTo("api.openweathermap.org");
        assertThat(built.getPath()).isEqualTo("/geo/1.0/direct");

        // Decode query params and assert values (robust to encoding style)
        var params = org.springframework.web.util.UriComponentsBuilder
                .fromUri(built)
                .build()
                .getQueryParams();

        String qRaw = params.getFirst("q");
        String qDecoded = java.net.URLDecoder.decode(qRaw, java.nio.charset.StandardCharsets.UTF_8);
        assertThat(qDecoded).isEqualTo("Buenos Aires,AR");

        assertThat(params.getFirst("limit")).isEqualTo("1");
        assertThat(params.getFirst("appid")).isEqualTo(apiKey);
    }

    @Test
    void returnsNull_whenApiReturnsEmptyArray() {
        RestTemplate http = mock(RestTemplate.class);
        OpenWeatherClient client = new OpenWeatherClient(http, "https://api.openweathermap.org", "K");
        when(http.getForObject(any(URI.class), eq(OpenWeatherDirectItemDto[].class)))
                .thenReturn(new OpenWeatherDirectItemDto[0]);

        OpenWeatherDirectItemDto out = client.fetchFirstMatch("Nowhere");

        assertThat(out).isNull();
    }

    @Test
    void returnsNull_whenApiReturnsNull() {
        RestTemplate http = mock(RestTemplate.class);
        OpenWeatherClient client = new OpenWeatherClient(http, "https://api.openweathermap.org", "K");
        when(http.getForObject(any(URI.class), eq(OpenWeatherDirectItemDto[].class))).thenReturn(null);

        OpenWeatherDirectItemDto out = client.fetchFirstMatch("Nowhere");

        assertThat(out).isNull();
    }
}
