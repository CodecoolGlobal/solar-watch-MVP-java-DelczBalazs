package com.codecool.solarwatch.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "cities",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_city_name_country_state", columnNames = {"name", "country", "state"})
        },
        indexes = {
                @Index(name = "idx_city_name", columnList = "name"),
                @Index(name = "idx_city_country", columnList = "country"),
                @Index(name = "idx_city_state", columnList = "state")
        }
)
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Keep raw name as provided by provider (case-insensitive search in repo)
    @Column(nullable = false)
    private String name;

    // ISO 3166-1 alpha-2/alpha-3 (store uppercase for consistency)
    @Column(length = 3)
    private String country;

    // Optional admin area (store as-is, can be null)
    private String state;

    // Prefer BigDecimal to avoid double rounding in DB
    /*@Column(precision = 9, scale = 6, nullable = false)*/
    private double lat;

    /*@Column(precision = 9, scale = 6, nullable = false)*/
    private double lon;

    // Optional provenance fields (nice on CV)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    protected City() {}

    public City(String name, String country, String state, double lat, double lon) {
        this.name = name;
        this.country = country != null ? country.toUpperCase() : null;
        this.state = state;
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

}
