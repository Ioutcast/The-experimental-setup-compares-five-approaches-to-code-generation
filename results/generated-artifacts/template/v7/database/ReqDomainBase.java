package generated.domain;

import java.math.BigDecimal;
import java.util.Objects;

/** Requirement: Сущности User, Department, Request, RequestType, ApprovalRoute и ApprovalStep. Coverage estimate: 0.78. */
public final class ReqDomainBase {
    private final String requestId;
    private final BigDecimal amount;
    private final String status;

    public ReqDomainBase(String requestId, BigDecimal amount, String status) {
        this.requestId = Objects.requireNonNull(requestId);
        this.amount = Objects.requireNonNull(amount);
        this.status = Objects.requireNonNull(status);
    }

    public boolean canBeSubmitted() {
        return "DRAFT".equals(status) && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}