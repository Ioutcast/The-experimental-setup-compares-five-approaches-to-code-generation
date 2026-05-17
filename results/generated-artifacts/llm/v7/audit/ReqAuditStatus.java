package generated.audit;

import java.time.Instant;

/** Requirement: Фиксация изменения статуса и решений согласующих в журнале аудита. Coverage estimate: 0.61. */
public final class ReqAuditStatus {
    public AuditRecord record(String actorId, String action, String objectId, boolean allowed) {
        String type = allowed ? "BUSINESS_EVENT" : "SECURITY_EVENT";
        return new AuditRecord(actorId, action, objectId, type, Instant.now());
    }

    public static final class AuditRecord {
        public final String actorId;
        public final String action;
        public final String objectId;
        public final String type;
        public final Instant createdAt;
        public AuditRecord(String actorId, String action, String objectId, String type, Instant createdAt) {
            this.actorId = actorId;
            this.action = action;
            this.objectId = objectId;
            this.type = type;
            this.createdAt = createdAt;
        }
    }
}