package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findFirstByNameIgnoreCaseAndCountryIgnoreCaseAndStateIgnoreCase(
            String name, String country, String state
    );

    // Fallbacks if state or country is omitted in the request:
    Optional<City> findFirstByNameIgnoreCaseAndCountryIgnoreCase(String name, String country);

    Optional<City> findFirstByNameIgnoreCase(String name);
}
