package com.codecool.solarwatch.ai;

import com.codecool.solarwatch.ai.client.AiClient;
import com.codecool.solarwatch.ai.client.GeminiAiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SolarWatchAiApplication.class, properties = {
        "ai.provider=gemini",
        "ai.gemini.model=gemini-2.5-flash"
})
class GeminiProviderSelectionTest {

    @Autowired
    ApplicationContext ctx;

    @Test
    void picksGeminiImplementation() {
        AiClient client = ctx.getBean(AiClient.class);
        assertThat(client).isInstanceOf(GeminiAiClient.class);
    }
}
