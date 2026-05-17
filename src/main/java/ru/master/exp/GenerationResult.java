package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Результат одного запуска генерации.
 */
final class GenerationResult {
    private final Approach approach;
    private final RequirementVersion version;
    private final int versionIndex;
    private final List<GeneratedArtifact> artifacts;
    private final Map<FeatureCategory, Double> categoryCoverage;
    private final int promptIterations;
    private final int manualRepairLoc;
    private final int regressionCount;
    private final int manualConflictCount;
    private final boolean externalModelCalled;
    private final String modelExcerpt;

    GenerationResult(
            Approach approach,
            RequirementVersion version,
            int versionIndex,
            List<GeneratedArtifact> artifacts,
            Map<FeatureCategory, Double> categoryCoverage,
            int promptIterations,
            int manualRepairLoc,
            int regressionCount,
            int manualConflictCount,
            boolean externalModelCalled,
            String modelExcerpt
    ) {
        this.approach = approach;
        this.version = version;
        this.versionIndex = versionIndex;
        this.artifacts = artifacts;
        this.categoryCoverage = categoryCoverage;
        this.promptIterations = promptIterations;
        this.manualRepairLoc = manualRepairLoc;
        this.regressionCount = regressionCount;
        this.manualConflictCount = manualConflictCount;
        this.externalModelCalled = externalModelCalled;
        this.modelExcerpt = modelExcerpt == null ? "" : modelExcerpt;
    }

    Approach approach() { return approach; }
    RequirementVersion version() { return version; }
    int versionIndex() { return versionIndex; }
    List<GeneratedArtifact> artifacts() { return artifacts; }
    Map<FeatureCategory, Double> categoryCoverage() { return categoryCoverage; }
    int promptIterations() { return promptIterations; }
    int manualRepairLoc() { return manualRepairLoc; }
    int regressionCount() { return regressionCount; }
    int manualConflictCount() { return manualConflictCount; }
    boolean externalModelCalled() { return externalModelCalled; }
    String modelExcerpt() { return modelExcerpt; }

    double averageCoverage() {
        return categoryCoverage.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    int generatedLoc() {
        return artifacts.stream().filter(GeneratedArtifact::generated).mapToInt(GeneratedArtifact::loc).sum();
    }

    int filesChanged() {
        return artifacts.size() + Math.max(0, manualRepairLoc / 45);
    }
}
