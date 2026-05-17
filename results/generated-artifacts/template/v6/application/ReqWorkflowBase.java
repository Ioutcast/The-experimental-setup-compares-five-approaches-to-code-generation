package generated.workflow;

import java.util.Map;
import java.util.Set;

/** Requirement: Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED. Coverage estimate: 0.28. */
public final class ReqWorkflowBase {
    private static final Map<String, Set<String>> TRANSITIONS = Map.of(
            "DRAFT", Set.of("SUBMITTED", "CANCELLED"),
            "SUBMITTED", Set.of("ROUTING"),
            "ROUTING", Set.of("IN_APPROVAL"),
            "IN_APPROVAL", Set.of("APPROVED", "REJECTED", "REVISION_REQUIRED"),
            "APPROVED", Set.of("EXECUTION_PENDING"),
            "EXECUTION_PENDING", Set.of("EXECUTED"),
            "EXECUTED", Set.of("CLOSED"));

    public boolean canTransit(String from, String to) {
        return TRANSITIONS.getOrDefault(from, Set.of()).contains(to);
    }
}