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
 * HTTP-клиент для вызова внешнего генератора по IP или URL.
 */
final class HttpModelClient implements ModelClient {
    private final String baseUrl;
    private final Duration timeout;
    private final ModelClient fallback;

    HttpModelClient(String baseUrl, Duration timeout, ModelClient fallback) {
        this.baseUrl = baseUrl == null ? "" : baseUrl.trim();
        this.timeout = timeout == null ? Duration.ofSeconds(5) : timeout;
        this.fallback = Objects.requireNonNull(fallback);
    }

    public ModelCompletion generate(ModelPrompt prompt) {
        if (baseUrl.isEmpty()) {
            return fallback.generate(prompt);
        }
        try {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout((int) timeout.toMillis());
            factory.setReadTimeout((int) timeout.toMillis());
            RestTemplate template = new RestTemplate(factory);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));

            ModelGenerationRequest request = new ModelGenerationRequest(
                    prompt.approach().name(),
                    prompt.version().code(),
                    prompt.requirements(),
                    prompt.layers()
            );
            ResponseEntity<ModelGenerationResponse> response = template.postForEntity(
                    trimTrailingSlash(baseUrl) + "/generate",
                    new HttpEntity<>(request, headers),
                    ModelGenerationResponse.class
            );
            ModelGenerationResponse body = response.getBody();
            if (body == null || body.getContent() == null || body.getContent().trim().isEmpty()) {
                return fallback.generate(prompt);
            }
            return new ModelCompletion(body.getContent(), body.getConfidence(), true);
        } catch (RuntimeException exception) {
            ModelCompletion fallbackCompletion = fallback.generate(prompt);
            return new ModelCompletion(
                    fallbackCompletion.content() + "\nRemote model call failed: " + exception.getClass().getSimpleName(),
                    fallbackCompletion.confidence(),
                    false
            );
        }
    }

    private String trimTrailingSlash(String value) {
        String result = value;
        while (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
