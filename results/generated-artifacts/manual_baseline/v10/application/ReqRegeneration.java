package generated.evolution;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Requirement: Повторная генерация без потери ручных изменений и с контролем регрессий. Coverage: 0.53. */
public final class ReqRegeneration {
    private static final String PROTECTED_START = "// <manual>";
    private static final String PROTECTED_END = "// </manual>";

    public RegenerationPlan plan(Path previousFile, Path generatedFile) throws Exception {
        Objects.requireNonNull(previousFile, "previousFile");
        Objects.requireNonNull(generatedFile, "generatedFile");
        List<String> manualBlocks = extractManualBlocks(previousFile);
        String checksum = checksum(generatedFile);
        return new RegenerationPlan(previousFile, generatedFile, manualBlocks, checksum);
    }

    private List<String> extractManualBlocks(Path file) throws Exception {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<String> blocks = new ArrayList<>();
        StringBuilder current = null;
        for (String line : lines) {
            if (line.contains(PROTECTED_START)) {
                current = new StringBuilder();
                continue;
            }
            if (line.contains(PROTECTED_END) && current != null) {
                blocks.add(current.toString());
                current = null;
                continue;
            }
            if (current != null) {
                current.append(line).append(System.lineSeparator());
            }
        }
        return blocks;
    }

    private String checksum(Path file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(Files.readAllBytes(file));
        StringBuilder result = new StringBuilder();
        for (byte item : hash) {
            result.append(String.format("%02x", item));
        }
        return result.toString();
    }

    public static final class RegenerationPlan {
        public final Path previousFile;
        public final Path generatedFile;
        public final List<String> manualBlocks;
        public final String generatedChecksum;

        private RegenerationPlan(Path previousFile, Path generatedFile, List<String> manualBlocks, String generatedChecksum) {
            this.previousFile = previousFile;
            this.generatedFile = generatedFile;
            this.manualBlocks = Collections.unmodifiableList(new ArrayList<>(manualBlocks));
            this.generatedChecksum = generatedChecksum;
        }
    }
}