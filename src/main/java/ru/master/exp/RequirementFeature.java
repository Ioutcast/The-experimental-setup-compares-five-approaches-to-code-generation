package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Отдельное требование внутри версии спецификации.
 */
final class RequirementFeature {
    private final String code;
    private final FeatureCategory category;
    private final String description;
    private final Set<Layer> layers;
    private final double complexity;

    RequirementFeature(String code, FeatureCategory category, String description, Set<Layer> layers, double complexity) {
        this.code = code;
        this.category = category;
        this.description = description;
        this.layers = layers;
        this.complexity = complexity;
    }

    String code() {
        return code;
    }

    FeatureCategory category() {
        return category;
    }

    String description() {
        return description;
    }

    Set<Layer> layers() {
        return layers;
    }

    double complexity() {
        return complexity;
    }
}
