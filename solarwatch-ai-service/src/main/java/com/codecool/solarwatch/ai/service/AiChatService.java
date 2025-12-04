package com.codecool.solarwatch.ai.service;

import com.codecool.solarwatch.ai.client.AiClient;
import org.springframework.stereotype.Service;

@Service
public class AiChatService {

    private final AiClient client;

    public AiChatService(AiClient client) {
        this.client = client;
    }

    public String ask(String city, String question) {
        return client.ask(city, question);
    }
}
