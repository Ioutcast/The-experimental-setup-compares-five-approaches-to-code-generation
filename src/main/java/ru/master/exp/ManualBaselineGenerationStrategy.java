package ru.master.exp;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Стратегия ручного эталона.
 */
final class ManualBaselineGenerationStrategy extends AbstractGenerationStrategy {
    ManualBaselineGenerationStrategy() {
        super(Approach.MANUAL_BASELINE, coverage(FeatureCategory.DOMAIN, 0.96, FeatureCategory.WORKFLOW, 0.95, FeatureCategory.ROUTING, 0.94, FeatureCategory.ACCESS, 0.93, FeatureCategory.SLA, 0.91, FeatureCategory.EVENT, 0.90, FeatureCategory.INTEGRATION, 0.88, FeatureCategory.AUDIT, 0.93, FeatureCategory.TEST, 0.88, FeatureCategory.EVOLUTION, 0.72), false, 0, 0.0, -0.004);
    }
}
