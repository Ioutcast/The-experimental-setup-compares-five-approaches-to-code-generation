package generated.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Requirement: Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep. Coverage: 0.85. */
public final class ReqDomainBase {
    private final String requestId;
    private final String authorId;
    private final BigDecimal amount;
    private final List<String> requiredFields;
    private String status;
    private Instant submittedAt;

    public ReqDomainBase(String requestId, String authorId, BigDecimal amount, List<String> requiredFields) {
        this.requestId = Objects.requireNonNull(requestId, "requestId");
        this.authorId = Objects.requireNonNull(authorId, "authorId");
        this.amount = requirePositive(amount);
        this.requiredFields = Collections.unmodifiableList(new ArrayList<>(requiredFields));
        this.status = "DRAFT";
    }

    public void submit(List<String> completedFields, Instant now) {
        ensureMutable();
        if (!completedFields.containsAll(requiredFields)) {
            throw new IllegalStateException("Request has incomplete required fields");
        }
        this.status = "SUBMITTED";
        this.submittedAt = Objects.requireNonNull(now, "now");
    }

    public void close() {
        if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
            throw new IllegalStateException("Only terminal approval decision can be closed");
        }
        this.status = "CLOSED";
    }

    public boolean belongsToAuthor(String actorId) {
        return authorId.equals(actorId);
    }

    public boolean isHighValue() {
        return amount.compareTo(new BigDecimal("500000")) >= 0;
    }

    private void ensureMutable() {
        if ("CLOSED".equals(status)) {
            throw new IllegalStateException("Closed request cannot be changed");
        }
    }

    private BigDecimal requirePositive(BigDecimal value) {
        Objects.requireNonNull(value, "amount");
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        return value;
    }

    public String status() {
        return status;
    }

    public Instant submittedAt() {
        return submittedAt;
    }
}