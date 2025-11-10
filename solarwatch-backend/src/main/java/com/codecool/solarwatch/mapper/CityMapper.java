package com.codecool.solarwatch.mapper;

import com.codecool.solarwatch.dto.admin.CityAdminDto;
import com.codecool.solarwatch.dto.admin.CityPatchDto;
import com.codecool.solarwatch.dto.admin.CityUpsertDto;
import com.codecool.solarwatch.entity.City;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityMapper {

    public City toEntity(CityUpsertDto d) {
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

    public CityAdminDto toDto(City e) {
        return new CityAdminDto(e.getId(), e.getName(), e.getCountry(), e.getState(), e.getLat(), e.getLon());
    }

    public List<CityAdminDto> toDtoList(List<City> entities) {
        return entities.stream().map(this::toDto).toList();
    }
}
