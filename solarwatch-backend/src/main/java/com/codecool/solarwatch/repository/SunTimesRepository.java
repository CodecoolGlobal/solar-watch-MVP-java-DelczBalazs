package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.entity.SunTimes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SunTimesRepository extends JpaRepository<SunTimes, Long> {

    Optional<SunTimes> findByCityAndDate(City city, LocalDate date);
}
