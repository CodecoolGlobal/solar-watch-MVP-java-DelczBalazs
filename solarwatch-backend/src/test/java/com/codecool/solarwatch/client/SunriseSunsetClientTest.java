package com.codecool.solarwatch.client;

import com.codecool.solarwatch.dto.sunrise.SunriseSunsetResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SunriseSunsetClientTest {

    @Test
    void buildsUri_andParsesUtcInstants() {
        // Arrange
        RestTemplate http = mock(RestTemplate.class);
        SunriseSunsetClient client = new SunriseSunsetClient(http, "https://api.sunrise-sunset.org");

        // API returns ISO timestamps with offset (formatted=0)
        SunriseSunsetResponseDto payload = new SunriseSunsetResponseDto(
                "OK",
                new SunriseSunsetResponseDto.ResultsDto(
                        "2025-08-23T03:00:00+00:00",
                        "2025-08-23T18:00:00+00:00"
                )
        );

        ArgumentCaptor<URI> uriCap = ArgumentCaptor.forClass(URI.class);
        when(http.getForObject(uriCap.capture(), eq(SunriseSunsetResponseDto.class))).thenReturn(payload);

        // Act
        var res = client.fetchUtc(-34.6037, -58.3816, LocalDate.of(2025, 8, 23));

        // Assert data
        assertThat(res.sunriseUtc()).isEqualTo(Instant.parse("2025-08-23T03:00:00Z"));
        assertThat(res.sunsetUtc()).isEqualTo(Instant.parse("2025-08-23T18:00:00Z"));

        // Assert URI
        URI built = uriCap.getValue();
        assertThat(built.getHost()).isEqualTo("api.sunrise-sunset.org");
        assertThat(built.getPath()).isEqualTo("/json");
        String qs = built.getQuery();
        // lat/lng/date present
        assertThat(qs).contains("lat=-34.6037");
        assertThat(qs).contains("lng=-58.3816");
        assertThat(qs).contains("date=2025-08-23");
        assertThat(qs).contains("formatted=0");
    }

    @Test
    void throws_whenApiReturnsNullOrNoResults() {
        RestTemplate http = mock(RestTemplate.class);
        SunriseSunsetClient client = new SunriseSunsetClient(http, "https://api.sunrise-sunset.org");

        when(http.getForObject(any(URI.class), eq(SunriseSunsetResponseDto.class))).thenReturn(null);

        assertThatThrownBy(() -> client.fetchUtc(0,0, LocalDate.of(2025,1,1)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("empty result");
    }
}
