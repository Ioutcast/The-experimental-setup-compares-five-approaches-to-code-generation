package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Запрос к внешнему генератору.
 */
final class ModelPrompt {
    private final Approach approach;
    private final RequirementVersion version;
    private final List<String> requirements;
    private final List<String> layers;

    ModelPrompt(Approach approach, RequirementVersion version, List<String> requirements, List<String> layers) {
        this.approach = approach;
        this.version = version;
        this.requirements = requirements;
        this.layers = layers;
    }

    Approach approach() {
        return approach;
    }

    RequirementVersion version() {
        return version;
    }

    List<String> requirements() {
        return requirements;
    }

    List<String> layers() {
        return layers;
    }
}
