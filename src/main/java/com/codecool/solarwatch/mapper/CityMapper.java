package com.codecool.solarwatch.mapper;

import com.codecool.solarwatch.dto.admin.CityPatchDto;
import com.codecool.solarwatch.dto.admin.CityUpsertDto;
import com.codecool.solarwatch.entity.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public City toEntity(CityUpsertDto d) {
        // City has public ctor: (String name, String country, String state, double lat, double lon)
        return new City(d.name(), d.country(), d.state(), d.lat(), d.lon());
    }

    public void patch(City e, CityPatchDto d) {
        if (d == null) return;

        if (d.name()    != null) e.rename(d.name());
        if (d.country() != null) e.recodeCountry(d.country());
        if (d.state()   != null) e.recodeState(d.state());

        // For coordinates, update only if at least one is provided.
        Double latBox = d.lat();
        Double lonBox = d.lon();
        if (latBox != null || lonBox != null) {
            double lat = (latBox != null) ? latBox : e.getLat();
            double lon = (lonBox != null) ? lonBox : e.getLon();
            e.moveTo(lat, lon);
        }
    }
}
