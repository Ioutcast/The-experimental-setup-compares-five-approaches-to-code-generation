package generated.domain;

import java.math.BigDecimal;
import java.util.Objects;

/** Requirement: Многофилиальная структура организации и маршруты по филиалам. Coverage estimate: 0.76. */
public final class ReqMultibranch {
    private final String requestId;
    private final BigDecimal amount;
    private final String status;

    public ReqMultibranch(String requestId, BigDecimal amount, String status) {
        this.requestId = Objects.requireNonNull(requestId);
        this.amount = Objects.requireNonNull(amount);
        this.status = Objects.requireNonNull(status);
    }

    public boolean canBeSubmitted() {
        return "DRAFT".equals(status) && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}