package ru.master.exp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Фабрика демонстрационных артефактов генерации.
 *
 * Файлы не подменяют промышленный кодогенератор. Они нужны, чтобы каждый
 * экспериментальный запуск оставлял проверяемые артефакты по слоям приложения.
 */
final class GeneratedArtifactFactory {
    private GeneratedArtifactFactory() {
    }

    static List<GeneratedArtifact> create(
            Approach approach,
            RequirementVersion version,
            Map<FeatureCategory, Double> coverage,
            boolean generated,
            String modelText
    ) {
        List<GeneratedArtifact> artifacts = new ArrayList<>();
        for (RequirementFeature feature : version.features()) {
            artifacts.add(artifactFor(approach, version, feature, coverage.getOrDefault(feature.category(), 0.5), generated));
        }
        artifacts.add(new GeneratedArtifact(
                Path.of("reports", approach.name().toLowerCase(Locale.ROOT), version.code(), "generation-summary.md"),
                Layer.TEST,
                summaryContent(approach, version, coverage, modelText),
                generated
        ));
        return artifacts;
    }

    private static GeneratedArtifact artifactFor(
            Approach approach,
            RequirementVersion version,
            RequirementFeature feature,
            double coverage,
            boolean generated
    ) {
        String className = toClassName(feature.code());
        Layer mainLayer = feature.layers().stream().min(Comparator.comparing(Enum::name)).orElse(Layer.APPLICATION);
        Path path = Path.of(
                approach.name().toLowerCase(Locale.ROOT),
                version.code(),
                mainLayer.name().toLowerCase(Locale.ROOT),
                className + ".java"
        );
        return new GeneratedArtifact(path, mainLayer, javaContent(className, feature, coverage), generated);
    }

    private static String javaContent(String className, RequirementFeature feature, double coverage) {
        switch (feature.category()) {
            case WORKFLOW:
                return workflowContent(className, feature, coverage);
            case ROUTING:
                return routingContent(className, feature, coverage);
            case ACCESS:
                return policyContent(className, feature, coverage);
            case AUDIT:
                return auditContent(className, feature, coverage);
            case SLA:
                return slaContent(className, feature, coverage);
            case INTEGRATION:
                return integrationContent(className, feature, coverage);
            case TEST:
                return testContent(className, feature, coverage);
            case EVENT:
                return eventContent(className, feature, coverage);
            case EVOLUTION:
                return evolutionContent(className, feature, coverage);
            case DOMAIN:
            default:
                return domainContent(className, feature, coverage);
        }
    }

    private static String domainContent(String className, RequirementFeature feature, double coverage) {
        return String.join("\n",
                "package generated.domain;",
                "",
                "/** Requirement: " + feature.description() + ". Coverage: " + pct(coverage) + ". */",
                "public final class " + className + " {",
                "    public boolean isValidDraft(String status) {",
                "        return \"DRAFT\".equals(status);",
                "    }",
                "}");
    }

    private static String workflowContent(String className, RequirementFeature feature, double coverage) {
        return String.join("\n",
                "package generated.workflow;",
                "",
                "import java.util.Map;",
                "import java.util.Set;",
                "",
                "/** Requirement: " + feature.description() + ". Coverage: " + pct(coverage) + ". */",
                "public final class " + className + " {",
                "    private static final Map<String, Set<String>> TRANSITIONS = Map.of(\"DRAFT\", Set.of(\"SUBMITTED\"));",
                "    public boolean canTransit(String from, String to) {",
                "        return TRANSITIONS.getOrDefault(from, Set.of()).contains(to);",
                "    }",
                "}");
    }

    private static String routingContent(String className, RequirementFeature feature, double coverage) {
        return String.join("\n",
                "package generated.workflow;",
                "",
                "import java.math.BigDecimal;",
                "import java.util.List;",
                "",
                "/** Requirement: " + feature.description() + ". Coverage: " + pct(coverage) + ". */",
                "public final class " + className + " {",
                "    public List<String> route(BigDecimal amount) {",
                "        return amount.compareTo(new BigDecimal(\"50000\")) >= 0 ? List.of(\"MANAGER\", \"FINANCE\") : List.of(\"MANAGER\");",
                "    }",
                "}");
    }

    private static String policyContent(String className, RequirementFeature feature, double coverage) {
        return String.join("\n",
                "package generated.policy;",
                "",
                "import java.util.Set;",
                "",
                "/** Requirement: " + feature.description() + ". Coverage: " + pct(coverage) + ". */",
                "public final class " + className + " {",
                "    public boolean canApprove(String actorId, String authorId, Set<String> roles, String role) {",
                "        return !actorId.equals(authorId) && roles.contains(role);",
                "    }",
                "}");
    }

    private static String auditContent(String className, RequirementFeature feature, double coverage) {
        return String.join("\n",
                "package generated.audit;",
                "",
                "/** Requirement: " + feature.description() + ". Coverage: " + pct(coverage) + ". */",
                "public final class " + className + " {",
                "    public String eventType(boolean allowed) {",
                "        return allowed ? \"BUSINESS_EVENT\" : \"SECURITY_EVENT\";",
                "    }",
                "}");
    }

    private static String slaContent(String className, RequirementFeature feature, double coverage) {
        return String.join("\n",
                "package generated.workflow;",
                "",
                "import java.time.Instant;",
                "",
                "/** Requirement: " + feature.description() + ". Coverage: " + pct(coverage) + ". */",
                "public final class " + className + " {",
                "    public boolean isViolated(Instant deadline, Instant now) {",
                "        return deadline.isBefore(now);",
                "    }",
                "}");
    }

    private static String integrationContent(String className, RequirementFeature feature, double coverage) {
        return String.join("\n",
                "package generated.integration;",
                "",
                "/** Requirement: " + feature.description() + ". Coverage: " + pct(coverage) + ". */",
                "public final class " + className + " {",
                "    public boolean shouldRetry(int attempt) {",
                "        return attempt < 3;",
                "    }",
                "}");
    }

    private static String eventContent(String className, RequirementFeature feature, double coverage) {
        return String.join("\n",
                "package generated.event;",
                "",
                "/** Requirement: " + feature.description() + ". Coverage: " + pct(coverage) + ". */",
                "public final class " + className + " {",
                "    public String eventName() {",
                "        return \"" + feature.category().name() + "\";",
                "    }",
                "}");
    }

    private static String testContent(String className, RequirementFeature feature, double coverage) {
        return String.join("\n",
                "package generated.test;",
                "",
                "/** Requirement: " + feature.description() + ". Coverage: " + pct(coverage) + ". */",
                "public final class " + className + "Test {",
                "    public boolean assertionExample() {",
                "        return true;",
                "    }",
                "}");
    }

    private static String evolutionContent(String className, RequirementFeature feature, double coverage) {
        return String.join("\n",
                "package generated.evolution;",
                "",
                "import java.nio.file.Path;",
                "",
                "/** Requirement: " + feature.description() + ". Coverage: " + pct(coverage) + ". */",
                "public final class " + className + " {",
                "    public boolean hasProtectedRegion(Path path) {",
                "        return path != null;",
                "    }",
                "}");
    }

    private static String summaryContent(
            Approach approach,
            RequirementVersion version,
            Map<FeatureCategory, Double> coverage,
            String modelText
    ) {
        String coverageLines = coverage.entrySet().stream()
                .map(entry -> "- " + entry.getKey().title() + ": " + pct(entry.getValue()))
                .collect(Collectors.joining("\n"));
        String modelPart = modelText == null || modelText.trim().isEmpty()
                ? "Model notes: not used."
                : "Model notes:\n" + modelText.lines().limit(12).collect(Collectors.joining("\n"));
        return String.join("\n",
                "# Generation summary",
                "",
                "Approach: " + approach.title(),
                "Requirement version: " + version.code() + " " + version.title(),
                "",
                "## Coverage",
                coverageLines,
                "",
                "## Notes",
                modelPart);
    }

    private static String toClassName(String code) {
        StringBuilder result = new StringBuilder();
        String[] parts = code.toLowerCase(Locale.ROOT).split("[^a-z0-9]+");
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            result.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                result.append(part.substring(1));
            }
        }
        return result.toString();
    }

    private static String pct(double value) {
        return String.format(Locale.US, "%.2f", value);
    }
}
