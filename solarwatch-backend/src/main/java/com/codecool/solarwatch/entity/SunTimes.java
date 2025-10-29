package com.codecool.solarwatch.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "sun_times",
        uniqueConstraints = @UniqueConstraint(columnNames = {"city_id", "date"})
)
public class SunTimes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) // forbid setId
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Instant sunriseUtc;

    @Column(nullable = false)
    private Instant sunsetUtc;

    @Column(nullable = false)
    private Integer dayLengthSec;

    // <-- SunTimesService expects this constructor
    public SunTimes(City city, LocalDate date, Instant sunriseUtc, Instant sunsetUtc, Integer dayLengthSec) {
        this.city = city;
        this.date = date;
        this.sunriseUtc = sunriseUtc;
        this.sunsetUtc = sunsetUtc;
        this.dayLengthSec = dayLengthSec;
    }

    // for admin edits
    public void reschedule(Instant sunriseUtc, Instant sunsetUtc, Integer dayLengthSec) {
        this.sunriseUtc = sunriseUtc;
        this.sunsetUtc = sunsetUtc;
        this.dayLengthSec = dayLengthSec;
    }
}
