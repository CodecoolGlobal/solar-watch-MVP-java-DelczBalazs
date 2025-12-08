package com.codecool.solarwatch.ai.client;

import com.codecool.solarwatch.ai.config.GeminiProperties;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Gemini implementation of AiClient using Google Gen AI Java SDK.
 * Reads API key from GOOGLE_API_KEY (handled by the SDK). Model is configured via ai.gemini.model.
 */
@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "gemini", matchIfMissing = true)
public class GeminiAiClient implements AiClient {

    private static final Logger log = LoggerFactory.getLogger(GeminiAiClient.class);
    private final GeminiProperties props;

    public GeminiAiClient(GeminiProperties props) {
        this.props = props;
        // SDK will pick up GOOGLE_API_KEY if present; default constructor uses Gemini Developer API backend
    }

    @Override
    public String ask(String city, String question) {
        String apiKey = System.getenv("GOOGLE_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            return "AI is not configured (missing GOOGLE_API_KEY).";
        }
        if (question == null || question.isBlank()) {
            return "Please ask a question.";
        }
        String model = props != null && props.model() != null && !props.model().isBlank() ? props.model() : "gemini-2.5-flash";

        String system = "You are SolarWatch Assistant. Answer concisely in 2-5 sentences." +
                (city != null && !city.isBlank() ? " The user is interested in the city '" + city.trim() + "'. Use it for context if relevant." : "");
        try {
            Client client = Client.builder().apiKey(apiKey).build();
            GenerateContentConfig cfg = GenerateContentConfig.builder()
                    .systemInstruction(Content.fromParts(Part.fromText(system)))
                    .build();

            GenerateContentResponse resp = client.models.generateContent(model, question, cfg);
            String text = resp == null ? null : resp.text();
            if (text == null || text.isBlank()) {
                return "Sorry, I couldn't generate an answer right now.";
            }
            return text.trim();
        } catch (Exception e) {
            log.warn("Gemini client error", e);
            String msg = e.getMessage();
            if (msg != null && msg.contains("429")) {
                return "The AI service is currently unavailable (quota exceeded). Demo only.";
            }
            return "The AI service is temporarily unavailable. Please try again later.";
        }
    }
}
