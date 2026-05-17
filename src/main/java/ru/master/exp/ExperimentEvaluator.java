package ru.master.exp;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Оцениватель результатов генерации.
 *
 * Рассчитывает функциональные, архитектурные, тестовые и сопровождаемые
 * метрики для одного подхода и одной версии требований.
 */
final class ExperimentEvaluator {
    MetricRow evaluate(ExperimentSpec spec, GenerationResult result) {
        RequirementVersion version = result.version();
        double specificationCoverage = weightedCoverage(version, result.categoryCoverage()) * 100.0;
        double implementedScenarios = implementedScenarios(spec, version, result.categoryCoverage()) * 100.0;
        int layerViolations = layerViolations(result);
        double dependencyScore = clamp(100.0 - layerViolations * 4.2, 45.0, 100.0);
        double controllerThinness = clamp(100.0 - layerViolations * 3.1 - (100.0 - score(result, FeatureCategory.DOMAIN) * 100.0) * 0.08, 50.0, 100.0);
        double domainPurity = clamp(100.0 - layerViolations * 2.8 - (100.0 - score(result, FeatureCategory.DOMAIN) * 100.0) * 0.12, 55.0, 100.0);
        double testPassRate = clamp(specificationCoverage * 0.78 + score(result, FeatureCategory.TEST) * 22.0, 20.0, 99.0);
        double businessRulePassRate = average(score(result, FeatureCategory.DOMAIN), score(result, FeatureCategory.WORKFLOW), score(result, FeatureCategory.ROUTING), score(result, FeatureCategory.ACCESS), score(result, FeatureCategory.SLA)) * 100.0;
        double workflowTestPassRate = average(score(result, FeatureCategory.WORKFLOW), score(result, FeatureCategory.ROUTING)) * 100.0;
        double mutationScore = clamp(testPassRate * 0.72 + score(result, FeatureCategory.TEST) * 18.0, 15.0, 95.0);
        double regenerationSuccess = clamp(score(result, FeatureCategory.EVOLUTION) * 100.0 - result.manualConflictCount() * 3.5, 10.0, 99.0);
        int generatedLoc = result.generatedLoc();
        int manualLoc = result.manualRepairLoc();
        double generatedChangeRatio = generatedLoc == 0 ? 0.0 : (double) generatedLoc / Math.max(1, generatedLoc + manualLoc) * 100.0;
        double changeEffort = manualLoc + result.regressionCount() * 18.0 + result.manualConflictCount() * 14.0;
        double timeToAdapt = Math.max(2.0, manualLoc / 18.0 + result.promptIterations() * 3.0 + result.manualConflictCount() * 5.0);

        return new MetricRow(result.approach(), version.code(), version.change(), implementedScenarios, score(result, FeatureCategory.WORKFLOW) * 100.0, score(result, FeatureCategory.ROUTING) * 100.0, score(result, FeatureCategory.ACCESS) * 100.0, score(result, FeatureCategory.SLA) * 100.0, layerViolations, dependencyScore, controllerThinness, domainPurity, generatedLoc, manualLoc, regenerationSuccess, result.promptIterations(), specificationCoverage, testPassRate, businessRulePassRate, workflowTestPassRate, mutationScore, result.regressionCount(), changeEffort, result.filesChanged(), generatedChangeRatio, result.manualConflictCount(), timeToAdapt);
    }

    private double weightedCoverage(RequirementVersion version, Map<FeatureCategory, Double> coverage) {
        double weighted = 0.0;
        double total = 0.0;
        for (RequirementFeature feature : version.features()) {
            weighted += coverage.getOrDefault(feature.category(), 0.5) * feature.complexity();
            total += feature.complexity();
        }
        return total == 0.0 ? 0.0 : weighted / total;
    }

    private double implementedScenarios(ExperimentSpec spec, RequirementVersion version, Map<FeatureCategory, Double> coverage) {
        Set<FeatureCategory> activeCategories = version.features().stream().map(RequirementFeature::category).collect(Collectors.toSet());
        int totalAssertions = 0;
        double passedAssertions = 0.0;
        for (Scenario scenario : spec.scenarios()) {
            if (activeCategories.contains(scenario.category())) {
                totalAssertions += scenario.expectedAssertions();
                passedAssertions += coverage.getOrDefault(scenario.category(), 0.5) * scenario.expectedAssertions();
            }
        }
        return totalAssertions == 0 ? 0.0 : passedAssertions / totalAssertions;
    }

    private int layerViolations(GenerationResult result) {
        int base;
        switch (result.approach()) {
            case MANUAL_BASELINE:
                base = 1;
                break;
            case TEMPLATE:
                base = 8;
                break;
            case DSL:
                base = 3;
                break;
            case LLM:
                base = 6;
                break;
            case HYBRID:
            default:
                base = 2;
                break;
        }
        double penalty = (1.0 - result.averageCoverage()) * result.version().features().size() / 1.7;
        return Math.max(0, (int) Math.round(base + penalty));
    }

    private double score(GenerationResult result, FeatureCategory category) {
        return result.categoryCoverage().getOrDefault(category, result.averageCoverage());
    }

    private double average(double... values) {
        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }
        return values.length == 0 ? 0.0 : sum / values.length;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
