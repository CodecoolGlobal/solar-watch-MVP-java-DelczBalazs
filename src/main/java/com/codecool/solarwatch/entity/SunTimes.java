package com.codecool.solarwatch.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "sun_times",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_city_date", columnNames = {"city_id", "date"})
        },
        indexes = {
                @Index(name = "idx_city_date", columnList = "city_id,date")
        }
)
public class SunTimes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many results over time per city
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false, foreignKey = @ForeignKey(name = "fk_suntimes_city"))
    private City city;

    // The local calendar date in the city’s location (you may also choose request date)
    @Column(nullable = false)
    private LocalDate date;

    // Canonical storage in UTC
    @Column(nullable = false)
    private Instant sunriseUtc;

    @Column(nullable = false)
    private Instant sunsetUtc;

    // Optional: useful for UI/debugging
    private Integer dayLengthSec;

    protected SunTimes() {}

    public SunTimes(City city, LocalDate date, Instant sunriseUtc, Instant sunsetUtc, Integer dayLengthSec) {
        this.city = city;
        this.date = date;
        this.sunriseUtc = sunriseUtc;
        this.sunsetUtc = sunsetUtc;
        this.dayLengthSec = dayLengthSec;
    }

    // getters/setters ...
}
