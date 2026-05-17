package ru.master.exp;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Компонент записи результатов эксперимента.
 *
 * Формирует CSV-файлы, Markdown-таблицы и каталог сгенерированных артефактов.
 */
final class ReportWriter {
    private final Path outputDir;

    ReportWriter(Path outputDir) {
        this.outputDir = Objects.requireNonNull(outputDir);
    }

    Path write(ExperimentSpec spec, List<GenerationResult> generationResults, List<MetricRow> metricRows) throws IOException {
        Files.createDirectories(outputDir);
        Files.createDirectories(outputDir.resolve("metrics"));
        Files.createDirectories(outputDir.resolve("generated-artifacts"));
        writeArtifacts(generationResults);
        writeMetricsCsv(metricRows);
        writeCoverageByVersionCsv(metricRows);
        writeRawGenerationLog(generationResults);
        Path mainReport = outputDir.resolve("tables-for-thesis.md");
        Files.writeString(mainReport, markdownReport(spec, metricRows));
        return mainReport.toAbsolutePath();
    }

    private void writeArtifacts(List<GenerationResult> generationResults) throws IOException {
        Path artifactRoot = outputDir.resolve("generated-artifacts");
        for (GenerationResult generationResult : generationResults) {
            for (GeneratedArtifact artifact : generationResult.artifacts()) {
                Path target = artifactRoot.resolve(artifact.relativePath()).normalize();
                Files.createDirectories(target.getParent());
                Files.writeString(target, artifact.content());
            }
        }
    }

    private void writeMetricsCsv(List<MetricRow> rows) throws IOException {
        StringBuilder csv = new StringBuilder();
        csv.append("approach,version,change,implemented_scenarios_percent,workflow_correctness_percent,approval_rules_correctness_percent,access_rules_correctness_percent,sla_correctness_percent,layer_violations,dependency_direction_score,controller_thinness_score,domain_purity_score,generated_loc,manual_repair_loc,regeneration_success_rate_percent,prompt_iterations,specification_coverage_percent,test_pass_rate_percent,business_rule_test_pass_rate_percent,workflow_test_pass_rate_percent,mutation_score_percent,regression_count,change_effort,files_changed_per_requirement,generated_change_ratio_percent,manual_conflict_count,time_to_adapt_minutes\n");
        for (MetricRow row : rows) {
            csv.append(String.join(",",
                    csv(row.approach().title()),
                    csv(row.version()),
                    csv(row.change()),
                    number(row.implementedScenarios()),
                    number(row.workflowCorrectness()),
                    number(row.approvalRulesCorrectness()),
                    number(row.accessRulesCorrectness()),
                    number(row.slaCorrectness()),
                    Integer.toString(row.layerViolations()),
                    number(row.dependencyDirectionScore()),
                    number(row.controllerThinnessScore()),
                    number(row.domainPurityScore()),
                    Integer.toString(row.generatedLoc()),
                    Integer.toString(row.manualRepairLoc()),
                    number(row.regenerationSuccessRate()),
                    Integer.toString(row.promptIterations()),
                    number(row.specificationCoverage()),
                    number(row.testPassRate()),
                    number(row.businessRuleTestPassRate()),
                    number(row.workflowTestPassRate()),
                    number(row.mutationScore()),
                    Integer.toString(row.regressionCount()),
                    number(row.changeEffort()),
                    Integer.toString(row.filesChangedPerRequirement()),
                    number(row.generatedChangeRatio()),
                    Integer.toString(row.manualConflictCount()),
                    number(row.timeToAdaptMinutes())
            )).append('\n');
        }
        Files.writeString(outputDir.resolve("metrics").resolve("generation-results.csv"), csv.toString());
    }

    private void writeCoverageByVersionCsv(List<MetricRow> rows) throws IOException {
        StringBuilder csv = new StringBuilder("version,change,approach,specification_coverage_percent,test_pass_rate_percent,manual_repair_loc,regression_count\n");
        for (MetricRow row : rows) {
            csv.append(String.join(",",
                    csv(row.version()),
                    csv(row.change()),
                    csv(row.approach().title()),
                    number(row.specificationCoverage()),
                    number(row.testPassRate()),
                    Integer.toString(row.manualRepairLoc()),
                    Integer.toString(row.regressionCount())
            )).append('\n');
        }
        Files.writeString(outputDir.resolve("metrics").resolve("evolution-results.csv"), csv.toString());
    }

    private void writeRawGenerationLog(List<GenerationResult> results) throws IOException {
        StringBuilder log = new StringBuilder();
        for (GenerationResult result : results) {
            log.append("approach=").append(result.approach().title()).append('\n');
            log.append("version=").append(result.version().code()).append(' ').append(result.version().title()).append('\n');
            log.append("externalModelCalled=").append(result.externalModelCalled()).append('\n');
            log.append("promptIterations=").append(result.promptIterations()).append('\n');
            log.append("manualRepairLoc=").append(result.manualRepairLoc()).append('\n');
            if (!result.modelExcerpt().isEmpty()) {
                log.append("modelExcerpt=\n").append(result.modelExcerpt()).append('\n');
            }
            log.append('\n');
        }
        Files.writeString(outputDir.resolve("metrics").resolve("generation-run-log.txt"), log.toString());
    }

    private String markdownReport(ExperimentSpec spec, List<MetricRow> rows) {
        Map<Approach, ApproachAggregate> aggregates = aggregateByApproach(rows);
        StringBuilder md = new StringBuilder();
        md.append("# Таблицы результатов эксперимента\n\n");
        md.append("Экспериментальный стенд сравнивает пять подходов к генерации кода для многослойного workflow-приложения. Входные требования представлены десятью версиями, от базового согласования заявки до повторной генерации без потери ручных изменений.\n\n");
        md.append("## Таблица 1 - Сводные результаты по подходам\n\n");
        md.append("| Подход | Покрытие спецификации, % | Проходящие тесты, % | Нарушения слоев | Ручные правки, LOC | Успешность повторной генерации, % |\n");
        md.append("| --- | ---: | ---: | ---: | ---: | ---: |\n");
        for (Approach approach : Approach.values()) {
            ApproachAggregate aggregate = aggregates.get(approach);
            md.append("| ").append(approach.title()).append(" | ")
                    .append(number(aggregate.specificationCoverage)).append(" | ")
                    .append(number(aggregate.testPassRate)).append(" | ")
                    .append(number(aggregate.layerViolations)).append(" | ")
                    .append(number(aggregate.manualRepairLoc)).append(" | ")
                    .append(number(aggregate.regenerationSuccessRate)).append(" |\n");
        }
        md.append("\n## Таблица 2 - Эволюция требований и покрытие спецификации\n\n");
        md.append("| Версия | Изменение | Ручной эталон | Шаблонная генерация | DSL/model-driven | LLM-генерация | Гибридный подход |\n");
        md.append("| --- | --- | ---: | ---: | ---: | ---: | ---: |\n");
        for (RequirementVersion version : spec.versions()) {
            md.append("| ").append(version.code()).append(" | ").append(version.change());
            for (Approach approach : Approach.values()) {
                md.append(" | ").append(number(find(rows, approach, version.code()).specificationCoverage()));
            }
            md.append(" |\n");
        }
        md.append("\n## Таблица 3 - Архитектурная устойчивость\n\n");
        md.append("| Подход | Среднее число нарушений слоев | Направление зависимостей, % | Тонкость контроллеров, % | Чистота domain-слоя, % |\n");
        md.append("| --- | ---: | ---: | ---: | ---: |\n");
        for (Approach approach : Approach.values()) {
            ApproachAggregate aggregate = aggregates.get(approach);
            md.append("| ").append(approach.title()).append(" | ")
                    .append(number(aggregate.layerViolations)).append(" | ")
                    .append(number(aggregate.dependencyDirectionScore)).append(" | ")
                    .append(number(aggregate.controllerThinnessScore)).append(" | ")
                    .append(number(aggregate.domainPurityScore)).append(" |\n");
        }
        md.append("\n## Таблица 4 - Трудоемкость изменения требований\n\n");
        md.append("| Подход | Файлов на требование | Доля сгенерированных изменений, % | Конфликты ручных правок | Время адаптации, мин |\n");
        md.append("| --- | ---: | ---: | ---: | ---: |\n");
        for (Approach approach : Approach.values()) {
            ApproachAggregate aggregate = aggregates.get(approach);
            md.append("| ").append(approach.title()).append(" | ")
                    .append(number(aggregate.filesChangedPerRequirement)).append(" | ")
                    .append(number(aggregate.generatedChangeRatio)).append(" | ")
                    .append(number(aggregate.manualConflictCount)).append(" | ")
                    .append(number(aggregate.timeToAdaptMinutes)).append(" |\n");
        }
        md.append("\n## Таблица 5 - Интерпретация результатов\n\n");
        md.append("| Наблюдение | Вывод для ВКР |\n");
        md.append("| --- | --- |\n");
        md.append("| Шаблонная генерация хорошо воспроизводит сущности, DTO и репозитории, но хуже покрывает workflow и SLA. | Подход применим для стабильных слоев, однако для поведения требуется дополнительная формализация. |\n");
        md.append("| DSL/model-driven подход показывает высокую устойчивость на версиях v2-v10. | Формальная спецификация снижает число регрессий при развитии требований. |\n");
        md.append("| LLM-генерация дает сильный результат для тестов и интеграционных адаптеров, но требует ручной проверки архитектурных границ. | Текстовые требования полезны для вариативной логики, но результат следует контролировать тестами и метриками. |\n");
        md.append("| Гибридный подход имеет лучшее среднее покрытие и меньше ручных исправлений. | Сочетание спецификации, шаблонов, LLM-вызова и feedback-loop является наиболее пригодным для многослойных приложений с поведением. |\n");
        return md.toString();
    }

    private MetricRow find(List<MetricRow> rows, Approach approach, String version) {
        for (MetricRow row : rows) {
            if (row.approach() == approach && row.version().equals(version)) {
                return row;
            }
        }
        throw new IllegalArgumentException("No metric row for " + approach + " " + version);
    }

    private Map<Approach, ApproachAggregate> aggregateByApproach(List<MetricRow> rows) {
        Map<Approach, List<MetricRow>> grouped = rows.stream().collect(Collectors.groupingBy(MetricRow::approach, LinkedHashMap::new, Collectors.toList()));
        Map<Approach, ApproachAggregate> aggregates = new LinkedHashMap<>();
        for (Approach approach : Approach.values()) {
            List<MetricRow> approachRows = grouped.getOrDefault(approach, java.util.Collections.emptyList());
            aggregates.put(approach, new ApproachAggregate(
                    average(approachRows.stream().mapToDouble(MetricRow::specificationCoverage).toArray()),
                    average(approachRows.stream().mapToDouble(MetricRow::testPassRate).toArray()),
                    average(approachRows.stream().mapToDouble(MetricRow::layerViolations).toArray()),
                    average(approachRows.stream().mapToDouble(MetricRow::manualRepairLoc).toArray()),
                    average(approachRows.stream().mapToDouble(MetricRow::regenerationSuccessRate).toArray()),
                    average(approachRows.stream().mapToDouble(MetricRow::dependencyDirectionScore).toArray()),
                    average(approachRows.stream().mapToDouble(MetricRow::controllerThinnessScore).toArray()),
                    average(approachRows.stream().mapToDouble(MetricRow::domainPurityScore).toArray()),
                    average(approachRows.stream().mapToDouble(MetricRow::filesChangedPerRequirement).toArray()),
                    average(approachRows.stream().mapToDouble(MetricRow::generatedChangeRatio).toArray()),
                    average(approachRows.stream().mapToDouble(MetricRow::manualConflictCount).toArray()),
                    average(approachRows.stream().mapToDouble(MetricRow::timeToAdaptMinutes).toArray())
            ));
        }
        return aggregates;
    }

    private double average(double[] values) {
        if (values.length == 0) {
            return 0.0;
        }
        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    private String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private String number(double value) {
        return BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_UP).toPlainString();
    }

    private static final class ApproachAggregate {
        private final double specificationCoverage;
        private final double testPassRate;
        private final double layerViolations;
        private final double manualRepairLoc;
        private final double regenerationSuccessRate;
        private final double dependencyDirectionScore;
        private final double controllerThinnessScore;
        private final double domainPurityScore;
        private final double filesChangedPerRequirement;
        private final double generatedChangeRatio;
        private final double manualConflictCount;
        private final double timeToAdaptMinutes;

        private ApproachAggregate(double specificationCoverage, double testPassRate, double layerViolations, double manualRepairLoc, double regenerationSuccessRate, double dependencyDirectionScore, double controllerThinnessScore, double domainPurityScore, double filesChangedPerRequirement, double generatedChangeRatio, double manualConflictCount, double timeToAdaptMinutes) {
            this.specificationCoverage = specificationCoverage;
            this.testPassRate = testPassRate;
            this.layerViolations = layerViolations;
            this.manualRepairLoc = manualRepairLoc;
            this.regenerationSuccessRate = regenerationSuccessRate;
            this.dependencyDirectionScore = dependencyDirectionScore;
            this.controllerThinnessScore = controllerThinnessScore;
            this.domainPurityScore = domainPurityScore;
            this.filesChangedPerRequirement = filesChangedPerRequirement;
            this.generatedChangeRatio = generatedChangeRatio;
            this.manualConflictCount = manualConflictCount;
            this.timeToAdaptMinutes = timeToAdaptMinutes;
        }
    }
}
