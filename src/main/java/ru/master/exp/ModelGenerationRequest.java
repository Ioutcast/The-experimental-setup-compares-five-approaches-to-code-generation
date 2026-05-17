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
 * DTO запроса к внешнему HTTP-генератору.
 */
final class ModelGenerationRequest {
    private String approach;
    private String version;
    private List<String> requirements;
    private List<String> layers;

    ModelGenerationRequest() {
    }

    ModelGenerationRequest(String approach, String version, List<String> requirements, List<String> layers) {
        this.approach = approach;
        this.version = version;
        this.requirements = requirements;
        this.layers = layers;
    }

    public String getApproach() { return approach; }
    public void setApproach(String approach) { this.approach = approach; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public List<String> getRequirements() { return requirements; }
    public void setRequirements(List<String> requirements) { this.requirements = requirements; }
    public List<String> getLayers() { return layers; }
    public void setLayers(List<String> layers) { this.layers = layers; }
}
