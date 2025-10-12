package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.dto.admin.SunTimesPatchDto;
import com.codecool.solarwatch.dto.admin.SunTimesUpsertDto;
import com.codecool.solarwatch.entity.SunTimes;
import com.codecool.solarwatch.service.SunTimesAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/suntimes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSunTimesController {

    private final SunTimesAdminService service;

    @GetMapping public List<SunTimes> all() { return service.findAll(); }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public SunTimes create(@Valid @RequestBody SunTimesUpsertDto dto) { return service.create(dto); }

    @PatchMapping("/{id}")
    public SunTimes patch(@PathVariable Long id, @RequestBody SunTimesPatchDto dto) { return service.patch(id, dto); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
