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
 * Клиенты генератора кода.
 */

/**
 * Общий интерфейс клиента генератора.
 */
interface ModelClient {
    ModelCompletion generate(ModelPrompt prompt);
}
