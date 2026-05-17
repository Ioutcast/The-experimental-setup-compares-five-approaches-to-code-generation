package ru.master.exp;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Стратегии генерации, участвующие в сравнительном эксперименте.
 */

/**
 * Общий контракт подхода генерации.
 */
interface GenerationStrategy {
    Approach approach();

    GenerationResult generate(ExperimentSpec spec, RequirementVersion version, int versionIndex);
}
