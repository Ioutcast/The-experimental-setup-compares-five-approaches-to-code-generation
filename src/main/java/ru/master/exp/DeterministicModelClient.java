package ru.master.exp;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Локальный детерминированный fallback-клиент.
 */
final class DeterministicModelClient implements ModelClient {
    public ModelCompletion generate(ModelPrompt prompt) {
        String requirements = prompt.requirements().stream()
                .limit(5)
                .map(item -> "- " + item)
                .collect(Collectors.joining("\n"));
        String content = String.join("\n",
                "Generated layered implementation plan:",
                "approach=" + prompt.approach().name(),
                "version=" + prompt.version().code(),
                "requirements:",
                requirements,
                "suggested layers=" + String.join(", ", prompt.layers()));
        double confidence;
        if (prompt.approach() == Approach.HYBRID) {
            confidence = 0.83;
        } else if (prompt.approach() == Approach.LLM) {
            confidence = 0.72;
        } else {
            confidence = 0.76;
        }
        return new ModelCompletion(content, confidence, false);
    }
}
