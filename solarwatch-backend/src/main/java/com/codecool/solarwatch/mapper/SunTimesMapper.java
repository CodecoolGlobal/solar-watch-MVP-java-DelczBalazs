package com.codecool.solarwatch.mapper;

import com.codecool.solarwatch.dto.admin.SunTimesPatchDto;
import com.codecool.solarwatch.dto.admin.SunTimesUpsertDto;
import com.codecool.solarwatch.entity.SunTimes;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SunTimesMapper {

    public SunTimes toEntity(SunTimesUpsertDto d) {
        throw new UnsupportedOperationException("Provide city/date to create SunTimes via service");
    }

    public void patch(SunTimes e, SunTimesPatchDto d) {
        if (d == null) return;
        Instant sunrise = (d.sunriseUtc() != null) ? d.sunriseUtc() : e.getSunriseUtc();
        Instant sunset  = (d.sunsetUtc()  != null) ? d.sunsetUtc()  : e.getSunsetUtc();
        Integer length  = (d.dayLengthSec()!= null) ? d.dayLengthSec() : e.getDayLengthSec();
        e.reschedule(sunrise, sunset, length);
    }
}
