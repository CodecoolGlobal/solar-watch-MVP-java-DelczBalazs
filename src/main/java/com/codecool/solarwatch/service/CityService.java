package com.codecool.solarwatch.service;

import com.codecool.solarwatch.client.OpenWeatherClient;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.model.openweather.OpenWeatherDirectItemDto;
import com.codecool.solarwatch.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityService {

    private final CityRepository cities;
    private final OpenWeatherClient openWeather;

    public CityService(CityRepository cities, OpenWeatherClient openWeather) {
        this.cities = cities;
        this.openWeather = openWeather;
    }

    public City findOrFetch(String city, String country, String state) {
        String nCity = normalizeCityName(city);
        String nCountry = normalizeCountry(country);
        String nState = normalizeState(state);

        // 1) DB lookup (prefer most specific)
        Optional<City> found = Optional.empty();
        if (nCity != null && nCountry != null && nState != null) {
            found = cities.findFirstByNameIgnoreCaseAndCountryIgnoreCaseAndStateIgnoreCase(nCity, nCountry, nState);
        }
        if (found.isEmpty() && nCity != null && nCountry != null) {
            found = cities.findFirstByNameIgnoreCaseAndCountryIgnoreCase(nCity, nCountry);
        }
        if (found.isEmpty() && nCity != null) {
            found = cities.findFirstByNameIgnoreCase(nCity);
        }
        if (found.isPresent()) return found.get();

        // 2) External provider
        String q = nCity + (nState != null ? ("," + nState) : "") + (nCountry != null ? ("," + nCountry) : "");
        OpenWeatherDirectItemDto dto = openWeather.fetchFirstMatch(q);
        if (dto == null) throw new CityNotFoundException(nCity);

        City toSave = new City(
                normalizeCityName(dto.name()),
                normalizeCountry(dto.country()),
                normalizeState(dto.state()),
                dto.lat(),
                dto.lon()
        );

        // 3) Last-chance dedupe before save (in case of race/normalize)
        return cities.findFirstByNameIgnoreCaseAndCountryIgnoreCaseAndStateIgnoreCase(
                toSave.getName(), toSave.getCountry(), toSave.getState()
        ).orElseGet(() -> cities.save(toSave));
    }

    private static String normalizeGeneric(String s) {
        return s == null ? null : s.replace('_', ' ').replaceAll("\\s+", " ").trim();
    }

    private static String normalizeCityName(String s) {
        return normalizeGeneric(s);
    }

    private static String normalizeState(String s) {
        return normalizeGeneric(s);
    }

    private static String normalizeCountry(String s) {
        return s == null ? null : s.trim().toUpperCase();
    }


}
