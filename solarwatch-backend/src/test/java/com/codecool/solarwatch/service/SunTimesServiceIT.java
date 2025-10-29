package com.codecool.solarwatch.service;

import com.codecool.solarwatch.client.SunriseSunsetClient;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.entity.SunTimes;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunTimesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@DataJpaTest
@Import(SunTimesService.class)
class SunTimesServiceIT {

    @Autowired
    private SunTimesService sunTimesService;

    @Autowired
    private SunTimesRepository sunRepo;

    @Autowired
    private CityRepository cityRepo;

    @MockBean
    private SunriseSunsetClient sunriseClient;

    @Test
    void findOrFetch_returnsFromDb_whenPresent() {
        // persist City first
        City city = cityRepo.save(new City("Miskolc", "HU", null, 48.1031, 20.7914));
        LocalDate date = LocalDate.of(2024, 9, 1);

        SunTimes stored = new SunTimes(
                city,
                date,
                Instant.parse("2024-09-01T04:55:00Z"),
                Instant.parse("2024-09-01T18:55:00Z"),
                (int) (Instant.parse("2024-09-01T18:55:00Z").getEpochSecond() - Instant.parse("2024-09-01T04:55:00Z").getEpochSecond())
        );

        sunRepo.save(stored); // valid now, city is managed

        SunTimes result = sunTimesService.findOrFetch(city, date, city.getLat(), city.getLon());

        assertThat(result.getId()).isNotNull();
        assertThat(result.getCity().getId()).isEqualTo(city.getId());
        assertThat(result.getSunriseUtc()).isEqualTo(stored.getSunriseUtc());
        assertThat(result.getSunsetUtc()).isEqualTo(stored.getSunsetUtc());
        assertThat(result.getDayLengthSec()).isEqualTo(stored.getDayLengthSec());
    }

    @Test
    void findOrFetch_callsRemoteAndPersists_whenMissingInDb() {
        // persist City first
        City city = cityRepo.save(new City("Eger", "HU", null, 47.9025, 20.3772));
        LocalDate date = LocalDate.of(2024, 5, 10);

        Instant rise = Instant.parse("2024-05-10T04:30:00Z");
        Instant set  = Instant.parse("2024-05-10T19:45:00Z");

        when(sunriseClient.fetchUtc(city.getLat(), city.getLon(), date))
                .thenReturn(new SunriseSunsetClient.UtcTimes(rise, set));

        SunTimes result = sunTimesService.findOrFetch(city, date, city.getLat(), city.getLon());

        assertThat(result.getId()).isNotNull();
        assertThat(result.getCity().getId()).isEqualTo(city.getId());
        assertThat(result.getDate()).isEqualTo(date);
        assertThat(result.getSunriseUtc()).isEqualTo(rise);
        assertThat(result.getSunsetUtc()).isEqualTo(set);
        assertThat(result.getDayLengthSec()).isEqualTo((int) (set.getEpochSecond() - rise.getEpochSecond()));

        assertThat(sunRepo.findByCityAndDate(city, date)).isPresent();
    }
}