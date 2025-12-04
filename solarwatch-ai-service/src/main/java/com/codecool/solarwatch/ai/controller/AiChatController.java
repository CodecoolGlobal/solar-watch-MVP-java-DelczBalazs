package com.codecool.solarwatch.ai.controller;

import com.codecool.solarwatch.ai.dto.AiChatRequest;
import com.codecool.solarwatch.ai.dto.AiChatResponse;
import com.codecool.solarwatch.ai.service.AiChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*") // TODO: lock down origins or add JWT validation later
public class AiChatController {

    private final AiChatService service;
    private static final Logger log = LoggerFactory.getLogger(AiChatController.class);

    public AiChatController(AiChatService service) {
        this.service = service;
    }

    @PostMapping("/chat")
    public AiChatResponse chat(@Valid @RequestBody AiChatRequest req) {
        String city = req.city();
        String q = req.question();
        String preview = q == null ? "" : (q.length() > 120 ? q.substring(0, 120) + "…" : q);
        log.info("/api/ai/chat request city='{}' q='{}'", city, preview);
        String answer = service.ask(req.city(), req.question());
        log.info("/api/ai/chat answer length={}", answer == null ? 0 : answer.length());
        return new AiChatResponse(answer);
    }
}
