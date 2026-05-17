package ru.master.exp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Артефакт, полученный в результате генерации.
 */
final class GeneratedArtifact {
    private final Path relativePath;
    private final Layer layer;
    private final String content;
    private final boolean generated;

    GeneratedArtifact(Path relativePath, Layer layer, String content, boolean generated) {
        this.relativePath = relativePath;
        this.layer = layer;
        this.content = content;
        this.generated = generated;
    }

    Path relativePath() {
        return relativePath;
    }

    Layer layer() {
        return layer;
    }

    String content() {
        return content;
    }

    boolean generated() {
        return generated;
    }

    int loc() {
        if (content == null || content.trim().isEmpty()) {
            return 0;
        }
        return (int) content.lines().filter(line -> !line.trim().isEmpty()).count();
    }
}
