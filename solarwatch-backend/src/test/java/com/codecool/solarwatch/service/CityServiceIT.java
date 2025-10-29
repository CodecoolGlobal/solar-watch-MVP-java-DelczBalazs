package com.codecool.solarwatch.service;

import com.codecool.solarwatch.client.OpenWeatherClient;
import com.codecool.solarwatch.dto.openweather.OpenWeatherDirectItemDto;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import(CityService.class)
class CityServiceIT {

    @Autowired
    private CityService cityService;

    @Autowired
    private CityRepository cityRepository;

    @MockBean
    private OpenWeatherClient openWeatherClient;

    @Test
    void findOrFetch_returnsExistingCity_whenAlreadyPersisted() {
        City saved = cityRepository.save(new City("Pécs", "HU", null, 46.0727, 18.2323));

        City result = cityService.findOrFetch("Pécs", "HU", null);

        assertThat(result.getId()).isEqualTo(saved.getId());
        assertThat(result.getName()).isEqualTo("Pécs");
    }

    @Test
    void findOrFetch_persistsFetchedCity_whenNotFoundLocally() {
        // amikor a DB üres, a service az openWeatherClient-et hívja:
        when(openWeatherClient.fetchFirstMatch("Debrecen,HU"))
                .thenReturn(new OpenWeatherDirectItemDto(
                        "Debrecen", 47.5316, 21.6273, "HU", null
                ));


        City result = cityService.findOrFetch("Debrecen", "HU", null);

        Optional<City> fromDb = cityRepository
                .findFirstByNameIgnoreCaseAndCountryIgnoreCase("Debrecen", "HU");

        assertThat(fromDb).isPresent();
        assertThat(result.getId()).isEqualTo(fromDb.get().getId());
        assertThat(result.getName()).isEqualTo("Debrecen");
    }

    @Test
    void duplicate_lookup_returnsSameEntity_forSameKey() {
        City a = cityRepository.save(new City("Győr", "HU", null, 47.68, 17.64));
        City b = cityService.findOrFetch("Győr", "HU", null);

        assertThat(b.getId()).isEqualTo(a.getId());
        assertThat(b.getName()).isEqualTo("Győr");
    }
}