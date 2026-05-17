package ru.master.exp;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Гибридная стратегия генерации.
 */
final class HybridGenerationStrategy extends AbstractGenerationStrategy {
    private final ModelClient modelClient;

    HybridGenerationStrategy(ModelClient modelClient) {
        super(Approach.HYBRID, coverage(FeatureCategory.DOMAIN, 0.95, FeatureCategory.WORKFLOW, 0.92, FeatureCategory.ROUTING, 0.88, FeatureCategory.ACCESS, 0.84, FeatureCategory.SLA, 0.82, FeatureCategory.EVENT, 0.86, FeatureCategory.INTEGRATION, 0.82, FeatureCategory.AUDIT, 0.87, FeatureCategory.TEST, 0.86, FeatureCategory.EVOLUTION, 0.90), true, 2, 18.0, 0.010);
        this.modelClient = Objects.requireNonNull(modelClient);
    }

    public GenerationResult generate(ExperimentSpec spec, RequirementVersion version, int versionIndex) {
        List<String> requirements = version.features().stream().map(RequirementFeature::description).collect(Collectors.toList());
        ModelCompletion completion = modelClient.generate(new ModelPrompt(approach(), version, requirements, Arrays.asList("domain", "workflow", "policy", "audit", "tests")));
        Map<FeatureCategory, Double> coverage = adjustedCoverage(version, versionIndex);
        for (Map.Entry<FeatureCategory, Double> entry : coverage.entrySet()) {
            entry.setValue(AbstractGenerationStrategy.clamp(entry.getValue() + (completion.confidence() - 0.60) * 0.035, 0.05, 0.99));
        }
        List<GeneratedArtifact> artifacts = GeneratedArtifactFactory.create(approach(), version, coverage, true, completion.content());
        return new GenerationResult(approach(), version, versionIndex, artifacts, coverage, 2 + Math.max(0, versionIndex / 5), manualRepairLoc(version, coverage), regressionCount(versionIndex, coverage), conflictCount(versionIndex, coverage), completion.remoteCallUsed(), firstLines(completion.content(), 4));
    }

    private String firstLines(String value, int limit) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }
        return value.lines().limit(limit).collect(Collectors.joining("\n"));
    }
}
