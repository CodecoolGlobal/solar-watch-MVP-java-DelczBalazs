package com.codecool.solarwatch.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SolarWatchAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SolarWatchAiApplication.class, args);
    }
}
