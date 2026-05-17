package generated.integration;

import java.time.Duration;

/** Requirement: Передача утвержденной заявки во внешнюю финансовую систему с retry-обработкой. Coverage estimate: 0.54. */
public final class ReqFinanceIntegration {
    public ExternalExecutionResult execute(String requestId, int attempt) {
        if (attempt < 3) return new ExternalExecutionResult(false, Duration.ofSeconds(2));
        return new ExternalExecutionResult(true, Duration.ZERO);
    }

    public static final class ExternalExecutionResult {
        public final boolean accepted;
        public final Duration retryAfter;
        public ExternalExecutionResult(boolean accepted, Duration retryAfter) {
            this.accepted = accepted;
            this.retryAfter = retryAfter;
        }
    }
}