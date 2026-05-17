package ru.master.exp;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ExperimentCoreTest {

    @TempDir
    Path tempDir;

    @Test
    void createsTenRequirementVersions() {
        ExperimentSpec spec = ExperimentSpecFactory.create();

        assertThat(spec.versions()).hasSize(10);
        assertThat(spec.versions().get(0).code()).isEqualTo("v1");
        assertThat(spec.versions().get(9).code()).isEqualTo("v10");
        assertThat(spec.scenarios()).isNotEmpty();
    }

    @Test
    void runnerWritesMetricsAndThesisTables() throws Exception {
        ExperimentSpec spec = ExperimentSpecFactory.create();
        ExperimentRunner runner = new ExperimentRunner(
                spec,
                List.of(
                        new ManualBaselineGenerationStrategy(),
                        new TemplateGenerationStrategy(),
                        new DslGenerationStrategy(),
                        new LlmGenerationStrategy(new DeterministicModelClient()),
                        new HybridGenerationStrategy(new DeterministicModelClient())
                ),
                new ExperimentEvaluator(),
                new ReportWriter(tempDir)
        );

        ExperimentSummary summary = runner.run();

        assertThat(summary.metricRows()).isEqualTo(50);
        assertThat(Files.exists(summary.mainReport())).isTrue();
        assertThat(Files.exists(tempDir.resolve("metrics").resolve("generation-results.csv"))).isTrue();
        assertThat(Files.readString(summary.mainReport())).contains("Таблица 1", "Гибридный подход");
    }
}
