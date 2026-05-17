package generated.workflow;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Requirement: Конечный автомат DRAFT, SUBMITTED, ROUTING, IN_APPROVAL, APPROVED, REJECTED и CLOSED. Coverage: 0.29. */
public final class ReqWorkflowBase {
    private final Map<String, Set<String>> transitions = new HashMap<>();

    public ReqWorkflowBase() {
        register("DRAFT", "SUBMITTED", "CANCELLED");
        register("SUBMITTED", "ROUTING");
        register("ROUTING", "IN_APPROVAL");
        register("IN_APPROVAL", "APPROVED", "REJECTED", "REVISION_REQUIRED");
        register("REVISION_REQUIRED", "DRAFT", "CANCELLED");
        register("APPROVED", "EXECUTION_PENDING");
        register("EXECUTION_PENDING", "EXECUTED", "REJECTED");
        register("EXECUTED", "CLOSED");
    }

    public TransitionResult transit(WorkflowContext context, String targetStatus, String actorId, Instant now) {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(targetStatus, "targetStatus");
        if (!transitions.getOrDefault(context.status, Collections.emptySet()).contains(targetStatus)) {
            return TransitionResult.rejected("Transition " + context.status + " -> " + targetStatus + " is not allowed");
        }
        if ("APPROVED".equals(targetStatus) && !context.allRequiredStepsApproved()) {
            return TransitionResult.rejected("Approval route is not completed");
        }
        WorkflowEvent event = new WorkflowEvent(context.requestId, context.status, targetStatus, actorId, now);
        return TransitionResult.applied(targetStatus, event);
    }

    private void register(String source, String... targets) {
        Set<String> allowed = transitions.computeIfAbsent(source, key -> new LinkedHashSet<>());
        Collections.addAll(allowed, targets);
    }

    public static final class WorkflowContext {
        public final String requestId;
        public final String status;
        public final List<ApprovalStep> route;

        public WorkflowContext(String requestId, String status, List<ApprovalStep> route) {
            this.requestId = Objects.requireNonNull(requestId, "requestId");
            this.status = Objects.requireNonNull(status, "status");
            this.route = Collections.unmodifiableList(new ArrayList<>(route));
        }

        boolean allRequiredStepsApproved() {
            return route.stream().allMatch(step -> "APPROVED".equals(step.decision));
        }
    }

    public static final class ApprovalStep {
        public final String role;
        public final String decision;

        public ApprovalStep(String role, String decision) {
            this.role = role;
            this.decision = decision;
        }
    }

    public static final class TransitionResult {
        public final boolean applied;
        public final String status;
        public final String rejectionReason;
        public final WorkflowEvent event;

        private TransitionResult(boolean applied, String status, String rejectionReason, WorkflowEvent event) {
            this.applied = applied;
            this.status = status;
            this.rejectionReason = rejectionReason;
            this.event = event;
        }

        static TransitionResult applied(String status, WorkflowEvent event) {
            return new TransitionResult(true, status, null, event);
        }

        static TransitionResult rejected(String reason) {
            return new TransitionResult(false, null, reason, null);
        }
    }

    public static final class WorkflowEvent {
        public final String requestId;
        public final String sourceStatus;
        public final String targetStatus;
        public final String actorId;
        public final Instant occurredAt;

        WorkflowEvent(String requestId, String sourceStatus, String targetStatus, String actorId, Instant occurredAt) {
            this.requestId = requestId;
            this.sourceStatus = sourceStatus;
            this.targetStatus = targetStatus;
            this.actorId = actorId;
            this.occurredAt = occurredAt;
        }
    }
}