package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.admin.SunTimesPatchDto;
import com.codecool.solarwatch.dto.admin.SunTimesUpsertDto;
import com.codecool.solarwatch.entity.SunTimes;
import com.codecool.solarwatch.mapper.SunTimesMapper;
import com.codecool.solarwatch.repository.SunTimesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class SunTimesAdminService {
    private final SunTimesRepository repo;
    private final SunTimesMapper mapper;

    public List<SunTimes> findAll() { return repo.findAll(); }

    @Transactional
    public SunTimes create(SunTimesUpsertDto dto) {
        SunTimes e = mapper.toEntity(dto);
        return repo.save(e);
    }

    @Transactional
    public SunTimes patch(Long id, SunTimesPatchDto dto) {
        SunTimes e = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("SunTimes not found: " + id));
        mapper.patch(e, dto);
        return e;
    }

    @Transactional
    public void delete(Long id) { repo.deleteById(id); }
}
