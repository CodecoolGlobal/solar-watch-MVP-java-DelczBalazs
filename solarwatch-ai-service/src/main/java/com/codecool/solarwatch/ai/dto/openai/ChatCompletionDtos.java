package com.codecool.solarwatch.ai.dto.openai;

import java.util.List;

public class ChatCompletionDtos {
    public record Message(String role, String content) {}
    public record Request(String model, List<Message> messages, Double temperature) {}

    public record Response(List<Choice> choices) {
        public record Choice(int index, Message message, Object logprobs, String finish_reason) {}
    }
}
