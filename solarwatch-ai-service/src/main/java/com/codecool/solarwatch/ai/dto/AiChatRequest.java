package com.codecool.solarwatch.ai.dto;

import jakarta.validation.constraints.NotBlank;

public record AiChatRequest(
        String city,
        @NotBlank(message = "question is required") String question
) {}
