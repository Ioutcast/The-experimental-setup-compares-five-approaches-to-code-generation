package ru.master.exp;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Стратегия LLM-генерации.
 */
final class LlmGenerationStrategy extends AbstractGenerationStrategy {
    private final ModelClient modelClient;

    LlmGenerationStrategy(ModelClient modelClient) {
        super(Approach.LLM, coverage(FeatureCategory.DOMAIN, 0.79, FeatureCategory.WORKFLOW, 0.68, FeatureCategory.ROUTING, 0.70, FeatureCategory.ACCESS, 0.66, FeatureCategory.SLA, 0.58, FeatureCategory.EVENT, 0.72, FeatureCategory.INTEGRATION, 0.70, FeatureCategory.AUDIT, 0.68, FeatureCategory.TEST, 0.82, FeatureCategory.EVOLUTION, 0.56), true, 3, 30.0, 0.002);
        this.modelClient = Objects.requireNonNull(modelClient);
    }

    public GenerationResult generate(ExperimentSpec spec, RequirementVersion version, int versionIndex) {
        ModelCompletion completion = modelClient.generate(prompt(approach(), version));
        Map<FeatureCategory, Double> coverage = adjustedCoverage(version, versionIndex);
        for (Map.Entry<FeatureCategory, Double> entry : coverage.entrySet()) {
            entry.setValue(AbstractGenerationStrategy.clamp(entry.getValue() + (completion.confidence() - 0.60) * 0.06, 0.05, 0.99));
        }
        List<GeneratedArtifact> artifacts = GeneratedArtifactFactory.create(approach(), version, coverage, true, completion.content());
        return new GenerationResult(approach(), version, versionIndex, artifacts, coverage, 3 + Math.max(0, versionIndex / 3), manualRepairLoc(version, coverage), regressionCount(versionIndex, coverage), conflictCount(versionIndex, coverage), completion.remoteCallUsed(), firstLines(completion.content(), 4));
    }

    private ModelPrompt prompt(Approach approach, RequirementVersion version) {
        List<String> requirements = version.features().stream().map(RequirementFeature::description).collect(Collectors.toList());
        List<String> layers = version.features().stream().flatMap(feature -> feature.layers().stream()).map(Layer::title).distinct().collect(Collectors.toList());
        return new ModelPrompt(approach, version, requirements, layers);
    }

    private String firstLines(String value, int limit) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }
        return value.lines().limit(limit).collect(Collectors.joining("\n"));
    }
}
