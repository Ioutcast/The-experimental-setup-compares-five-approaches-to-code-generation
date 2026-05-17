package ru.master.exp;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа экспериментального стенда.
 *
 * Приложение запускается как CLI-задача Spring Boot: создает спецификацию
 * требований, выполняет сравнение подходов генерации и записывает результаты
 * эксперимента в каталог {@code results}.
 */
@SpringBootApplication
public class CodeGenerationExperimentApplication implements CommandLineRunner {

    @Value("${experiment.output-dir:results}")
    private String outputDir;

    @Value("${model.api.base-url:}")
    private String modelBaseUrl;

    @Value("${model.api.timeout-ms:5000}")
    private long modelTimeoutMs;

    /**
     * Запускает Spring Boot приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(CodeGenerationExperimentApplication.class, args);
    }

    /**
     * Выполняет полный эксперимент после инициализации Spring-контекста.
     *
     * @param args аргументы командной строки
     * @throws Exception если не удалось сформировать отчеты
     */
    @Override
    public void run(String... args) throws Exception {
        var spec = ExperimentSpecFactory.create();
        ModelClient modelClient = new HttpModelClient(
                modelBaseUrl,
                Duration.ofMillis(modelTimeoutMs),
                new DeterministicModelClient()
        );

        var runner = new ExperimentRunner(
                spec,
                List.of(
                        new ManualBaselineGenerationStrategy(),
                        new TemplateGenerationStrategy(),
                        new DslGenerationStrategy(),
                        new LlmGenerationStrategy(modelClient),
                        new HybridGenerationStrategy(modelClient)
                ),
                new ExperimentEvaluator(),
                new ReportWriter(Path.of(outputDir))
        );

        ExperimentSummary summary = runner.run();

        System.out.println("Experiment completed");
        System.out.println("Requirement versions: " + summary.requirementVersions());
        System.out.println("Approaches: " + summary.approaches());
        System.out.println("Metric rows: " + summary.metricRows());
        System.out.println("Main report: " + summary.mainReport());
    }
}
