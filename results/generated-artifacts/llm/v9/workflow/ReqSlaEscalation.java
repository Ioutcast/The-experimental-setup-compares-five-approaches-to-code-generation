package generated.workflow;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/** Requirement: Контроль сроков согласования, уведомления и эскалация при нарушении SLA. Coverage: 0.47. */
public final class ReqSlaEscalation {
    public SlaDecision evaluate(SlaContext context, Instant now) {
        Objects.requireNonNull(context, "context");
        Instant deadline = context.assignedAt.plus(context.allowedDuration);
        if (!deadline.isBefore(now)) {
            return SlaDecision.active(deadline);
        }
        Instant escalationDeadline = deadline.plus(context.escalationGracePeriod);
        if (escalationDeadline.isBefore(now)) {
            return SlaDecision.escalate(deadline, escalationDeadline);
        }
        return SlaDecision.warning(deadline);
    }

    public static final class SlaContext {
        public final String requestId;
        public final String stepRole;
        public final Instant assignedAt;
        public final Duration allowedDuration;
        public final Duration escalationGracePeriod;

        public SlaContext(String requestId, String stepRole, Instant assignedAt, Duration allowedDuration, Duration escalationGracePeriod) {
            this.requestId = Objects.requireNonNull(requestId, "requestId");
            this.stepRole = Objects.requireNonNull(stepRole, "stepRole");
            this.assignedAt = Objects.requireNonNull(assignedAt, "assignedAt");
            this.allowedDuration = Objects.requireNonNull(allowedDuration, "allowedDuration");
            this.escalationGracePeriod = Objects.requireNonNull(escalationGracePeriod, "escalationGracePeriod");
        }
    }

    public static final class SlaDecision {
        public final String status;
        public final Instant deadline;
        public final Instant escalationDeadline;

        private SlaDecision(String status, Instant deadline, Instant escalationDeadline) {
            this.status = status;
            this.deadline = deadline;
            this.escalationDeadline = escalationDeadline;
        }

        static SlaDecision active(Instant deadline) {
            return new SlaDecision("ACTIVE", deadline, null);
        }

        static SlaDecision warning(Instant deadline) {
            return new SlaDecision("WARNING", deadline, null);
        }

        static SlaDecision escalate(Instant deadline, Instant escalationDeadline) {
            return new SlaDecision("ESCALATE", deadline, escalationDeadline);
        }
    }
}