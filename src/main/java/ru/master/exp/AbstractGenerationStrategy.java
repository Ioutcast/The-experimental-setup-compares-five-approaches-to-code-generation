package ru.master.exp;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Базовая стратегия с общей моделью расчета покрытия, ручных исправлений,
 * регрессий и конфликтов.
 */
abstract class AbstractGenerationStrategy implements GenerationStrategy {
    private final Approach approach;
    private final Map<FeatureCategory, Double> baseCoverage;
    private final boolean generatedCode;
    private final int basePromptIterations;
    private final double manualRepairFactor;
    private final double feedbackGain;

    AbstractGenerationStrategy(
            Approach approach,
            Map<FeatureCategory, Double> baseCoverage,
            boolean generatedCode,
            int basePromptIterations,
            double manualRepairFactor,
            double feedbackGain
    ) {
        this.approach = approach;
        this.baseCoverage = baseCoverage;
        this.generatedCode = generatedCode;
        this.basePromptIterations = basePromptIterations;
        this.manualRepairFactor = manualRepairFactor;
        this.feedbackGain = feedbackGain;
    }

    public Approach approach() {
        return approach;
    }

    public GenerationResult generate(ExperimentSpec spec, RequirementVersion version, int versionIndex) {
        Map<FeatureCategory, Double> coverage = adjustedCoverage(version, versionIndex);
        List<GeneratedArtifact> artifacts = GeneratedArtifactFactory.create(approach, version, coverage, generatedCode, "");
        return new GenerationResult(
                approach,
                version,
                versionIndex,
                artifacts,
                coverage,
                basePromptIterations,
                manualRepairLoc(version, coverage),
                regressionCount(versionIndex, coverage),
                conflictCount(versionIndex, coverage),
                false,
                ""
        );
    }

    Map<FeatureCategory, Double> adjustedCoverage(RequirementVersion version, int versionIndex) {
        Map<FeatureCategory, Double> coverage = new EnumMap<>(FeatureCategory.class);
        for (FeatureCategory category : FeatureCategory.values()) {
            double base = baseCoverage.containsKey(category) ? baseCoverage.get(category) : 0.55;
            double complexityPenalty = averageComplexity(version, category) * 0.012;
            double evolutionPenalty = Math.max(0, versionIndex - 1) * 0.012;
            double feedbackBonus = Math.max(0, versionIndex - 1) * feedbackGain;
            coverage.put(category, clamp(base - complexityPenalty - evolutionPenalty + feedbackBonus, 0.05, 0.99));
        }
        return coverage;
    }

    int manualRepairLoc(RequirementVersion version, Map<FeatureCategory, Double> coverage) {
        double weightedDeficit = 0.0;
        for (RequirementFeature feature : version.features()) {
            weightedDeficit += (1.0 - coverage.getOrDefault(feature.category(), 0.5)) * feature.complexity();
        }
        if (!generatedCode) {
            return (int) Math.round(90 + version.features().size() * 42 + totalComplexity(version) * 12);
        }
        return (int) Math.round(weightedDeficit * manualRepairFactor);
    }

    int regressionCount(int versionIndex, Map<FeatureCategory, Double> coverage) {
        double average = coverage.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return Math.max(0, (int) Math.round((1.0 - average) * versionIndex * 1.2 - 1.0));
    }

    int conflictCount(int versionIndex, Map<FeatureCategory, Double> coverage) {
        double evolutionScore = coverage.getOrDefault(FeatureCategory.EVOLUTION, 0.5);
        return Math.max(0, (int) Math.round((1.0 - evolutionScore) * Math.max(0, versionIndex - 3) / 2.2));
    }

    private double averageComplexity(RequirementVersion version, FeatureCategory category) {
        return version.features().stream()
                .filter(feature -> feature.category() == category)
                .mapToDouble(RequirementFeature::complexity)
                .average()
                .orElse(1.0);
    }

    private double totalComplexity(RequirementVersion version) {
        return version.features().stream().mapToDouble(RequirementFeature::complexity).sum();
    }

    static Map<FeatureCategory, Double> coverage(Object... entries) {
        Map<FeatureCategory, Double> coverage = new EnumMap<>(FeatureCategory.class);
        for (int i = 0; i < entries.length; i += 2) {
            coverage.put((FeatureCategory) entries[i], (Double) entries[i + 1]);
        }
        return coverage;
    }

    static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
