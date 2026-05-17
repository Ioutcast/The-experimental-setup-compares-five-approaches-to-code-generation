package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Проверяемый бизнес-сценарий.
 */
final class Scenario {
    private final String code;
    private final FeatureCategory category;
    private final String title;
    private final int expectedAssertions;

    Scenario(String code, FeatureCategory category, String title, int expectedAssertions) {
        this.code = code;
        this.category = category;
        this.title = title;
        this.expectedAssertions = expectedAssertions;
    }

    String code() {
        return code;
    }

    FeatureCategory category() {
        return category;
    }

    String title() {
        return title;
    }

    int expectedAssertions() {
        return expectedAssertions;
    }
}
