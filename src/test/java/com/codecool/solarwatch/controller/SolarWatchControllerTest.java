package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.SunTimesResponseDto;
import com.codecool.solarwatch.service.SolarWatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolarWatchController.class)
class SolarWatchControllerTest {

    @Autowired MockMvc mvc;
    @MockBean SolarWatchService service;

    @Test
    void getSunTimes_happyPath_mapsParamsAndReturnsJson() throws Exception {
        SunTimesResponseDto mock = new SunTimesResponseDto(
                "Budapest","HU",null,
                new SunTimesResponseDto.Coordinates(47.49,19.04),
                LocalDate.parse("2025-08-23"),
                "Europe/Budapest",
                OffsetDateTime.parse("2025-08-23T05:49:23+02:00"),
                OffsetDateTime.parse("2025-08-23T19:43:28+02:00"),
                new SunTimesResponseDto.SourceMeta("openweathermap","sunrise-sunset.org")
        );
        when(service.getSunTimes(eq("Budapest"), eq("HU"), isNull(), eq(LocalDate.parse("2025-08-23")), eq("Europe/Budapest")))
                .thenReturn(mock);

        mvc.perform(get("/api/solarwatch")
                        .param("city","Budapest")
                        .param("country","HU")
                        .param("date","2025-08-23")
                        .param("tz","Europe/Budapest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Budapest"))
                .andExpect(jsonPath("$.timezone").value("Europe/Budapest"))
                .andExpect(jsonPath("$.sunrise").value("2025-08-23T05:49:23+02:00"));
    }

    @Test
    void getSunTimes_defaultTzIsUTC_whenNotProvided() throws Exception {
        when(service.getSunTimes(eq("Budapest"), eq("HU"), isNull(), any(), eq("UTC")))
                .thenReturn(new SunTimesResponseDto(
                        "Budapest","HU",null,
                        new SunTimesResponseDto.Coordinates(47.49,19.04),
                        LocalDate.now(),
                        "UTC",
                        OffsetDateTime.parse("2025-08-23T03:49:23Z"),
                        OffsetDateTime.parse("2025-08-23T17:43:28Z"),
                        new SunTimesResponseDto.SourceMeta("openweathermap","sunrise-sunset.org")
                ));

        mvc.perform(get("/api/solarwatch")
                        .param("city","Budapest")
                        .param("country","HU"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timezone").value("UTC"));
    }
}
