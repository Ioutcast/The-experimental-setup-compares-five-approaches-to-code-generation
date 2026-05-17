package generated.workflow;

import java.time.Duration;
import java.time.Instant;

/** Requirement: Контроль сроков согласования, уведомления и эскалация при нарушении SLA. Coverage estimate: 0.83. */
public final class ReqSlaEscalation {
    public boolean isViolated(Instant assignedAt, Duration allowedDuration, Instant now) {
        return assignedAt.plus(allowedDuration).isBefore(now);
    }

    public boolean requiresEscalation(Instant assignedAt, Duration allowedDuration, Instant now) {
        return assignedAt.plus(allowedDuration).plus(Duration.ofHours(24)).isBefore(now);
    }
}