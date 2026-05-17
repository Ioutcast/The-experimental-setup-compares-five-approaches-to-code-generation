package generated.audit;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Requirement: Фиксация изменения статуса и решений согласующих в журнале аудита. Coverage: 0.62. */
public final class ReqAuditStatus {
    private final List<AuditEntry> entries = new ArrayList<>();

    public AuditEntry recordBusinessEvent(String actorId, String requestId, String action, String details) {
        return append("BUSINESS_EVENT", actorId, requestId, action, details, true);
    }

    public AuditEntry recordSecurityViolation(String actorId, String requestId, String action, String reason) {
        return append("SECURITY_EVENT", actorId, requestId, action, reason, false);
    }

    public List<AuditEntry> entriesForRequest(String requestId) {
        List<AuditEntry> result = new ArrayList<>();
        for (AuditEntry entry : entries) {
            if (entry.requestId.equals(requestId)) {
                result.add(entry);
            }
        }
        return Collections.unmodifiableList(result);
    }

    private AuditEntry append(String type, String actorId, String requestId, String action, String message, boolean allowed) {
        AuditEntry entry = new AuditEntry(UUID.randomUUID().toString(), type, actorId, requestId, action, message, allowed, Instant.now());
        entries.add(entry);
        return entry;
    }

    public static final class AuditEntry {
        public final String auditId;
        public final String type;
        public final String actorId;
        public final String requestId;
        public final String action;
        public final String message;
        public final boolean allowed;
        public final Instant createdAt;

        private AuditEntry(String auditId, String type, String actorId, String requestId, String action, String message, boolean allowed, Instant createdAt) {
            this.auditId = auditId;
            this.type = type;
            this.actorId = Objects.requireNonNull(actorId, "actorId");
            this.requestId = Objects.requireNonNull(requestId, "requestId");
            this.action = Objects.requireNonNull(action, "action");
            this.message = Objects.requireNonNull(message, "message");
            this.allowed = allowed;
            this.createdAt = createdAt;
        }
    }
}