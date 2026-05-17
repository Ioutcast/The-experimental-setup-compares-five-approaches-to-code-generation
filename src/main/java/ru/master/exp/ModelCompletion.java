package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Ответ генератора.
 */
final class ModelCompletion {
    private final String content;
    private final double confidence;
    private final boolean remoteCallUsed;

    ModelCompletion(String content, double confidence, boolean remoteCallUsed) {
        this.content = content;
        this.confidence = confidence;
        this.remoteCallUsed = remoteCallUsed;
    }

    String content() {
        return content;
    }

    double confidence() {
        return confidence;
    }

    boolean remoteCallUsed() {
        return remoteCallUsed;
    }
}
