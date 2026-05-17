package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Полная спецификация эксперимента.
 */
final class ExperimentSpec {
    private final List<RequirementVersion> versions;
    private final List<Scenario> scenarios;

    ExperimentSpec(List<RequirementVersion> versions, List<Scenario> scenarios) {
        this.versions = versions;
        this.scenarios = scenarios;
    }

    List<RequirementVersion> versions() {
        return versions;
    }

    List<Scenario> scenarios() {
        return scenarios;
    }
}
