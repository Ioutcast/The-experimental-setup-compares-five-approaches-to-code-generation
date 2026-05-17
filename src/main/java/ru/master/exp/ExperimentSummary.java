package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Сводка выполнения эксперимента.
 */
final class ExperimentSummary {
    private final int requirementVersions;
    private final int approaches;
    private final int metricRows;
    private final Path mainReport;

    ExperimentSummary(int requirementVersions, int approaches, int metricRows, Path mainReport) {
        this.requirementVersions = requirementVersions;
        this.approaches = approaches;
        this.metricRows = metricRows;
        this.mainReport = mainReport;
    }

    int requirementVersions() { return requirementVersions; }
    int approaches() { return approaches; }
    int metricRows() { return metricRows; }
    Path mainReport() { return mainReport; }
}
