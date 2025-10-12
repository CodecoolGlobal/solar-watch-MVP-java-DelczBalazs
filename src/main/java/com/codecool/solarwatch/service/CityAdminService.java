package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.admin.CityPatchDto;
import com.codecool.solarwatch.dto.admin.CityUpsertDto;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.mapper.CityMapper;
import com.codecool.solarwatch.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class CityAdminService {
    private final CityRepository repo;
    private final CityMapper mapper;

    public List<City> findAll() { return repo.findAll(); }

    @Transactional
    public City create(CityUpsertDto dto) {
        City e = mapper.toEntity(dto);
        return repo.save(e);
    }

    @Transactional
    public City patch(Long id, CityPatchDto dto) {
        City e = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found: " + id));
        mapper.patch(e, dto);
        return e; // JPA dirty checking persists
    }

    @Transactional
    public void delete(Long id) { repo.deleteById(id); }
}
