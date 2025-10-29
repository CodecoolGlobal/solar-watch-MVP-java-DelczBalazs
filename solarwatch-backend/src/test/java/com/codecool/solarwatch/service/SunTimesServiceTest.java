package com.codecool.solarwatch.service;

import com.codecool.solarwatch.client.SunriseSunsetClient;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.entity.SunTimes;
import com.codecool.solarwatch.repository.SunTimesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SunTimesServiceTest {

    @Mock SunTimesRepository sunRepo;
    @Mock SunriseSunsetClient sunClient;

    @InjectMocks SunTimesService service;

    @Test
    void returnsDbHit_whenPresent() {
        var city = new City("Budapest", "HU", null, 47.4979, 19.0402);
        var day = LocalDate.of(2025, 8, 23);
        var existing = new SunTimes(city, day, Instant.EPOCH, Instant.EPOCH.plusSeconds(3600), 3600);

        when(sunRepo.findByCityAndDate(city, day)).thenReturn(Optional.of(existing));

        var out = service.findOrFetch(city, day, 47.4979, 19.0402);

        assertThat(out).isSameAs(existing);
        verifyNoInteractions(sunClient);
    }

    @Test
    void fetchesAndSaves_whenMissing() {
        var city = new City("Budapest", "HU", null, 47.4979, 19.0402);
        var day = LocalDate.of(2025, 8, 23);

        when(sunRepo.findByCityAndDate(city, day)).thenReturn(Optional.empty());
        when(sunClient.fetchUtc(47.4979, 19.0402, day))
                .thenReturn(new SunriseSunsetClient.UtcTimes(Instant.EPOCH, Instant.EPOCH.plusSeconds(4000)));
        when(sunRepo.save(any(SunTimes.class))).thenAnswer(inv -> inv.getArgument(0));

        var out = service.findOrFetch(city, day, 47.4979, 19.0402);

        assertThat(out.getSunriseUtc()).isEqualTo(Instant.EPOCH);
        assertThat(out.getSunsetUtc()).isEqualTo(Instant.EPOCH.plusSeconds(4000));
        assertThat(out.getDayLengthSec()).isEqualTo(4000);

        verify(sunRepo).save(any(SunTimes.class));
    }
}
