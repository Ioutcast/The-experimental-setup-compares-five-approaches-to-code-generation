package ru.master.exp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Исполнитель эксперимента.
 *
 * Запускает все стратегии для всех версий требований, передает результаты
 * оценивателю и инициирует запись отчетов.
 */
final class ExperimentRunner {
    private final ExperimentSpec spec;
    private final List<GenerationStrategy> strategies;
    private final ExperimentEvaluator evaluator;
    private final ReportWriter reportWriter;

    ExperimentRunner(ExperimentSpec spec, List<GenerationStrategy> strategies, ExperimentEvaluator evaluator, ReportWriter reportWriter) {
        this.spec = Objects.requireNonNull(spec);
        this.strategies = new ArrayList<>(strategies);
        this.evaluator = Objects.requireNonNull(evaluator);
        this.reportWriter = Objects.requireNonNull(reportWriter);
    }

    ExperimentSummary run() throws IOException {
        List<GenerationResult> generationResults = new ArrayList<>();
        List<MetricRow> metricRows = new ArrayList<>();
        for (int i = 0; i < spec.versions().size(); i++) {
            RequirementVersion version = spec.versions().get(i);
            int versionIndex = i + 1;
            for (GenerationStrategy strategy : strategies) {
                GenerationResult generationResult = strategy.generate(spec, version, versionIndex);
                generationResults.add(generationResult);
                metricRows.add(evaluator.evaluate(spec, generationResult));
            }
        }
        Path mainReport = reportWriter.write(spec, generationResults, metricRows);
        return new ExperimentSummary(spec.versions().size(), strategies.size(), metricRows.size(), mainReport);
    }
}
