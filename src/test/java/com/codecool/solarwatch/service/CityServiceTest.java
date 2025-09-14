package com.codecool.solarwatch.service;

import com.codecool.solarwatch.client.OpenWeatherClient;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.model.openweather.OpenWeatherDirectItemDto;
import com.codecool.solarwatch.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock CityRepository cityRepo;
    @Mock OpenWeatherClient openWeather;

    @InjectMocks CityService service;

    @Test
    void returnsDbHit_whenCityExists_exactMatch() {
        var stored = new City("New York", "US", "New York", 40.7128, -74.0060);

        when(cityRepo.findFirstByNameIgnoreCaseAndCountryIgnoreCaseAndStateIgnoreCase("New York", "US", "New York"))
                .thenReturn(Optional.of(stored));

        var out = service.findOrFetch("New York", "US", "New York");

        assertThat(out).isSameAs(stored);
        verifyNoInteractions(openWeather);
    }

    @Test
    void fetchesAndSaves_whenCityMissing_countryOnly() {
        // DB miss for (Budapest, HU)
        when(cityRepo.findFirstByNameIgnoreCaseAndCountryIgnoreCase("Budapest", "HU"))
                .thenReturn(Optional.empty());

        // External API returns coords
        when(openWeather.fetchFirstMatch("Budapest,HU"))
                .thenReturn(new OpenWeatherDirectItemDto("Budapest", 47.4979, 19.0402, "HU", null));

        // Last-chance dedupe still empty → we will save
        when(cityRepo.findFirstByNameIgnoreCaseAndCountryIgnoreCaseAndStateIgnoreCase("Budapest", "HU", null))
                .thenReturn(Optional.empty());

        // Save returns the same instance (common testing trick)
        when(cityRepo.save(any(City.class))).thenAnswer(inv -> inv.getArgument(0));

        var out = service.findOrFetch("Budapest", "HU", null);

        assertThat(out.getName()).isEqualTo("Budapest");
        assertThat(out.getCountry()).isEqualTo("HU");
        assertThat(out.getLat()).isEqualTo(47.4979);
        assertThat(out.getLon()).isEqualTo(19.0402);

        verify(openWeather).fetchFirstMatch("Budapest,HU");
        verify(cityRepo).save(any(City.class));
    }

    @Test
    void throwsCityNotFound_whenApiReturnsEmpty() {
        when(cityRepo.findFirstByNameIgnoreCase("Atlantis")).thenReturn(Optional.empty());
        when(openWeather.fetchFirstMatch("Atlantis")).thenReturn(null);

        assertThatThrownBy(() -> service.findOrFetch("Atlantis", null, null))
                .isInstanceOf(CityNotFoundException.class);
    }

    @Test
    void normalizesUnderscoresAndSpaces_cityName() {
        // If you implemented normalization, "New_York" should match DB "New York"
        var stored = new City("New York", "US", "New York", 1.0, 1.0);
        when(cityRepo.findFirstByNameIgnoreCase("New York")).thenReturn(Optional.of(stored));

        var out = service.findOrFetch("New_York", null, null);

        assertThat(out.getName()).isEqualTo("New York");
        verifyNoInteractions(openWeather);
    }
}
