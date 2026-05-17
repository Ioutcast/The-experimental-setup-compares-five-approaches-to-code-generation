package ru.master.exp;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Стратегия DSL/model-driven генерации.
 */
final class DslGenerationStrategy extends AbstractGenerationStrategy {
    DslGenerationStrategy() {
        super(Approach.DSL, coverage(FeatureCategory.DOMAIN, 0.93, FeatureCategory.WORKFLOW, 0.90, FeatureCategory.ROUTING, 0.84, FeatureCategory.ACCESS, 0.70, FeatureCategory.SLA, 0.78, FeatureCategory.EVENT, 0.74, FeatureCategory.INTEGRATION, 0.63, FeatureCategory.AUDIT, 0.76, FeatureCategory.TEST, 0.66, FeatureCategory.EVOLUTION, 0.83), true, 0, 24.0, 0.006);
    }
}
