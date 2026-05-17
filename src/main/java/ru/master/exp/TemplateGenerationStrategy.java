package ru.master.exp;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Стратегия шаблонной генерации.
 */
final class TemplateGenerationStrategy extends AbstractGenerationStrategy {
    TemplateGenerationStrategy() {
        super(Approach.TEMPLATE, coverage(FeatureCategory.DOMAIN, 0.88, FeatureCategory.WORKFLOW, 0.38, FeatureCategory.ROUTING, 0.42, FeatureCategory.ACCESS, 0.46, FeatureCategory.SLA, 0.30, FeatureCategory.EVENT, 0.40, FeatureCategory.INTEGRATION, 0.43, FeatureCategory.AUDIT, 0.58, FeatureCategory.TEST, 0.55, FeatureCategory.EVOLUTION, 0.48), true, 0, 38.0, 0.000);
    }
}
