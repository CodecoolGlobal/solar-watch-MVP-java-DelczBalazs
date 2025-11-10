package com.codecool.solarwatch.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA
@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    private String state;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lon;

    // CityService expects this constructor
    public City(String name, String country, String state, double lat, double lon) {
        this.name = name;
        this.country = country;
        this.state = state;
        this.lat = lat;
        this.lon = lon;
    }

    // for admin edits
    public void rename(String newName) { this.name = newName; }
    public void recodeCountry(String newCountry) { this.country = newCountry; }
    public void recodeState(String newState) { this.state = newState; }
    public void moveTo(double newLat, double newLon) { this.lat = newLat; this.lon = newLon; }
}
