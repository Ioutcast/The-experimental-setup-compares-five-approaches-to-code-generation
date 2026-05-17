package generated.domain;

import java.math.BigDecimal;
import java.util.Objects;

/** Requirement: Настраиваемые типы заявок и обязательные поля без изменения кода контроллеров. Coverage estimate: 0.80. */
public final class ReqConfigurableTypes {
    private final String requestId;
    private final BigDecimal amount;
    private final String status;

    public ReqConfigurableTypes(String requestId, BigDecimal amount, String status) {
        this.requestId = Objects.requireNonNull(requestId);
        this.amount = Objects.requireNonNull(amount);
        this.status = Objects.requireNonNull(status);
    }

    public boolean canBeSubmitted() {
        return "DRAFT".equals(status) && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}