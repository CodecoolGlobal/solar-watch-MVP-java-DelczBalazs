package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.dto.admin.CityPatchDto;
import com.codecool.solarwatch.dto.admin.CityUpsertDto;
import com.codecool.solarwatch.entity.City;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.service.CityAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cities")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCityController {

    private final CityAdminService service;

    @GetMapping public List<City> all() { return service.findAll(); }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public City create(@Valid @RequestBody CityUpsertDto dto) { return service.create(dto); }

    @PatchMapping("/{id}")
    public City patch(@PathVariable Long id, @RequestBody CityPatchDto dto) { return service.patch(id, dto); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
