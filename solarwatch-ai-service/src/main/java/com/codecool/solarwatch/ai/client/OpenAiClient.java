package com.codecool.solarwatch.ai.client;

import com.codecool.solarwatch.ai.config.AiProperties;
import com.codecool.solarwatch.ai.dto.openai.ChatCompletionDtos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "openai")
public class OpenAiClient implements AiClient {

    private final RestTemplate http;
    private final AiProperties props;
    private static final Logger log = LoggerFactory.getLogger(OpenAiClient.class);

    public OpenAiClient(RestTemplate http, AiProperties props) {
        this.http = http;
        this.props = props;
    }

    @Override
    public String ask(String city, String question) {
        if (props == null || props.apiKey() == null || props.apiKey().isBlank()) {
            return "AI is not configured (missing API key).";
        }
        String system = "You are SolarWatch Assistant. Answer concisely in 2-5 sentences."
                + (city != null && !city.isBlank() ? " The user is interested in the city '" + city.trim() + "'. Use it for context if relevant." : "");

        var messages = List.of(
                new ChatCompletionDtos.Message("system", system),
                new ChatCompletionDtos.Message("user", question)
        );
        var body = new ChatCompletionDtos.Request(props.model(), messages, 0.2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(props.apiKey());

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                var resp = http.postForObject(props.apiUrl(), new HttpEntity<>(body, headers), ChatCompletionDtos.Response.class);
                if (resp == null || resp.choices() == null || resp.choices().isEmpty() || resp.choices().get(0).message() == null) {
                    return "Sorry, I couldn't generate an answer right now.";
                }
                return resp.choices().get(0).message().content();
            } catch (HttpStatusCodeException e) {
                int status = e.getStatusCode().value();
                boolean retryable = status == 429 || e.getStatusCode().is5xxServerError();
                if (retryable && attempt < 3) {
                    log.warn("OpenAI HTTP {} on attempt {}/3. Retrying...", status, attempt);
                    try { Thread.sleep(250L * (1L << (attempt - 1))); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    continue;
                }
                try {
                    var bodyText = e.getResponseBodyAsString();
                    log.warn("OpenAI HTTP {} error. Body: {}", status, bodyText);
                } catch (Exception ignore) {
                    log.warn("OpenAI HTTP {} error.", status);
                }
                if (status == 429) {
                    return "The AI service is currently unavailable (quota exceeded). Demo only.";
                }
                return "The AI service is temporarily unavailable (" + status + "). Please try again later.";
            } catch (Exception e) {
                log.error("OpenAI client error (attempt {}/3)", attempt, e);
                if (attempt < 3) {
                    try { Thread.sleep(250L * (1L << (attempt - 1))); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    continue;
                }
                return "The AI service encountered an error. Please try again.";
            }
        }
        return "The AI service encountered an unexpected condition.";
    }
}
