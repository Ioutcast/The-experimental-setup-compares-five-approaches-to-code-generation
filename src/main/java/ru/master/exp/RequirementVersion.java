package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Версия требований к экспериментальному приложению.
 */
final class RequirementVersion {
    private final String code;
    private final String title;
    private final String change;
    private final List<RequirementFeature> features;

    RequirementVersion(String code, String title, String change, List<RequirementFeature> features) {
        this.code = code;
        this.title = title;
        this.change = change;
        this.features = features;
    }

    String code() {
        return code;
    }

    String title() {
        return title;
    }

    String change() {
        return change;
    }

    List<RequirementFeature> features() {
        return features;
    }
}
