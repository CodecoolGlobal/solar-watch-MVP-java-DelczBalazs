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
        // 1) DB lookup (prefer most specific)
        Optional<City> found = Optional.empty();
        if (city != null && country != null && state != null) {
            found = cities.findFirstByNameIgnoreCaseAndCountryIgnoreCaseAndStateIgnoreCase(city, country, state);
        }
        if (found.isEmpty() && city != null && country != null) {
            found = cities.findFirstByNameIgnoreCaseAndCountryIgnoreCase(city, country);
        }
        if (found.isEmpty() && city != null) {
            found = cities.findFirstByNameIgnoreCase(city);
        }
        if (found.isPresent()) return found.get();

        // 2) External provider
        String q = city + (state != null ? ("," + state) : "") + (country != null ? ("," + country) : "");
        OpenWeatherDirectItemDto dto = openWeather.fetchFirstMatch(q);
        if (dto == null) throw new CityNotFoundException(city);

        City toSave = new City(
                dto.name(),
                dto.country(),
                dto.state(),
                dto.lat(),
                dto.lon()
        );

        // 3) Save and return
        return cities.save(toSave);
    }
}
