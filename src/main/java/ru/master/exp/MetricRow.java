package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Строка итоговой таблицы метрик.
 */
final class MetricRow {
    private final Approach approach;
    private final String version;
    private final String change;
    private final double implementedScenarios;
    private final double workflowCorrectness;
    private final double approvalRulesCorrectness;
    private final double accessRulesCorrectness;
    private final double slaCorrectness;
    private final int layerViolations;
    private final double dependencyDirectionScore;
    private final double controllerThinnessScore;
    private final double domainPurityScore;
    private final int generatedLoc;
    private final int manualRepairLoc;
    private final double regenerationSuccessRate;
    private final int promptIterations;
    private final double specificationCoverage;
    private final double testPassRate;
    private final double businessRuleTestPassRate;
    private final double workflowTestPassRate;
    private final double mutationScore;
    private final int regressionCount;
    private final double changeEffort;
    private final int filesChangedPerRequirement;
    private final double generatedChangeRatio;
    private final int manualConflictCount;
    private final double timeToAdaptMinutes;

    MetricRow(
            Approach approach,
            String version,
            String change,
            double implementedScenarios,
            double workflowCorrectness,
            double approvalRulesCorrectness,
            double accessRulesCorrectness,
            double slaCorrectness,
            int layerViolations,
            double dependencyDirectionScore,
            double controllerThinnessScore,
            double domainPurityScore,
            int generatedLoc,
            int manualRepairLoc,
            double regenerationSuccessRate,
            int promptIterations,
            double specificationCoverage,
            double testPassRate,
            double businessRuleTestPassRate,
            double workflowTestPassRate,
            double mutationScore,
            int regressionCount,
            double changeEffort,
            int filesChangedPerRequirement,
            double generatedChangeRatio,
            int manualConflictCount,
            double timeToAdaptMinutes
    ) {
        this.approach = approach;
        this.version = version;
        this.change = change;
        this.implementedScenarios = implementedScenarios;
        this.workflowCorrectness = workflowCorrectness;
        this.approvalRulesCorrectness = approvalRulesCorrectness;
        this.accessRulesCorrectness = accessRulesCorrectness;
        this.slaCorrectness = slaCorrectness;
        this.layerViolations = layerViolations;
        this.dependencyDirectionScore = dependencyDirectionScore;
        this.controllerThinnessScore = controllerThinnessScore;
        this.domainPurityScore = domainPurityScore;
        this.generatedLoc = generatedLoc;
        this.manualRepairLoc = manualRepairLoc;
        this.regenerationSuccessRate = regenerationSuccessRate;
        this.promptIterations = promptIterations;
        this.specificationCoverage = specificationCoverage;
        this.testPassRate = testPassRate;
        this.businessRuleTestPassRate = businessRuleTestPassRate;
        this.workflowTestPassRate = workflowTestPassRate;
        this.mutationScore = mutationScore;
        this.regressionCount = regressionCount;
        this.changeEffort = changeEffort;
        this.filesChangedPerRequirement = filesChangedPerRequirement;
        this.generatedChangeRatio = generatedChangeRatio;
        this.manualConflictCount = manualConflictCount;
        this.timeToAdaptMinutes = timeToAdaptMinutes;
    }

    Approach approach() { return approach; }
    String version() { return version; }
    String change() { return change; }
    double implementedScenarios() { return implementedScenarios; }
    double workflowCorrectness() { return workflowCorrectness; }
    double approvalRulesCorrectness() { return approvalRulesCorrectness; }
    double accessRulesCorrectness() { return accessRulesCorrectness; }
    double slaCorrectness() { return slaCorrectness; }
    int layerViolations() { return layerViolations; }
    double dependencyDirectionScore() { return dependencyDirectionScore; }
    double controllerThinnessScore() { return controllerThinnessScore; }
    double domainPurityScore() { return domainPurityScore; }
    int generatedLoc() { return generatedLoc; }
    int manualRepairLoc() { return manualRepairLoc; }
    double regenerationSuccessRate() { return regenerationSuccessRate; }
    int promptIterations() { return promptIterations; }
    double specificationCoverage() { return specificationCoverage; }
    double testPassRate() { return testPassRate; }
    double businessRuleTestPassRate() { return businessRuleTestPassRate; }
    double workflowTestPassRate() { return workflowTestPassRate; }
    double mutationScore() { return mutationScore; }
    int regressionCount() { return regressionCount; }
    double changeEffort() { return changeEffort; }
    int filesChangedPerRequirement() { return filesChangedPerRequirement; }
    double generatedChangeRatio() { return generatedChangeRatio; }
    int manualConflictCount() { return manualConflictCount; }
    double timeToAdaptMinutes() { return timeToAdaptMinutes; }
}
