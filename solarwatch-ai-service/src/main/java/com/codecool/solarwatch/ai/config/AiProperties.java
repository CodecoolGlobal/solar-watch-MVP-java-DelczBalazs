package com.codecool.solarwatch.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai.openai")
public record AiProperties(
        String apiUrl,
        String apiKey,
        String model
) {}
