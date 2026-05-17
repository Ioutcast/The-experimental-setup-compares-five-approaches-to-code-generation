package generated.integration;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Requirement: Передача утвержденной заявки во внешнюю финансовую систему с retry-обработкой. Coverage: 0.32. */
public final class ReqFinanceIntegration {
    private static final int MAX_ATTEMPTS = 3;
    private static final Duration BASE_RETRY_DELAY = Duration.ofSeconds(2);

    private final FinanceGateway gateway;
    private final OutboxPublisher outboxPublisher;
    private final IntegrationAudit audit;

    public ReqFinanceIntegration(FinanceGateway gateway, OutboxPublisher outboxPublisher, IntegrationAudit audit) {
        this.gateway = Objects.requireNonNull(gateway, "gateway");
        this.outboxPublisher = Objects.requireNonNull(outboxPublisher, "outboxPublisher");
        this.audit = Objects.requireNonNull(audit, "audit");
    }

    public FinanceTransferResult transferApprovedRequest(ApprovedRequest request) {
        validate(request);
        List<AttemptLog> attempts = new ArrayList<>();
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                FinanceTransferCommand command = toCommand(request, attempt);
                FinanceGatewayResponse response = gateway.send(command);
                attempts.add(AttemptLog.success(attempt, response.externalDocumentId));
                audit.recordSuccess(request.requestId, response.externalDocumentId, attempt);
                outboxPublisher.publish(IntegrationEvent.transferred(request.requestId, response.externalDocumentId));
                return FinanceTransferResult.accepted(response.externalDocumentId, attempts);
            } catch (TemporaryFinanceException exception) {
                attempts.add(AttemptLog.retry(attempt, exception.getMessage(), retryDelay(attempt)));
                audit.recordRetry(request.requestId, attempt, exception.getMessage());
                if (attempt == MAX_ATTEMPTS) {
                    outboxPublisher.publish(IntegrationEvent.failed(request.requestId, exception.getMessage()));
                    return FinanceTransferResult.retryExhausted(attempts);
                }
            } catch (PermanentFinanceException exception) {
                attempts.add(AttemptLog.rejected(attempt, exception.getMessage()));
                audit.recordFailure(request.requestId, attempt, exception.getMessage());
                outboxPublisher.publish(IntegrationEvent.failed(request.requestId, exception.getMessage()));
                return FinanceTransferResult.rejected(exception.getMessage(), attempts);
            }
        }
        return FinanceTransferResult.retryExhausted(attempts);
    }

    private FinanceTransferCommand toCommand(ApprovedRequest request, int attempt) {
        return new FinanceTransferCommand(UUID.randomUUID().toString(), request.requestId, request.departmentCode, request.amount, request.currency, attempt, Instant.now());
    }

    private Duration retryDelay(int attempt) {
        return BASE_RETRY_DELAY.multipliedBy(attempt);
    }

    private void validate(ApprovedRequest request) {
        Objects.requireNonNull(request, "request");
        if (!"APPROVED".equals(request.status)) {
            throw new IllegalArgumentException("Only approved requests can be sent to finance system");
        }
        if (request.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    public interface FinanceGateway {
        FinanceGatewayResponse send(FinanceTransferCommand command);
    }

    public interface OutboxPublisher {
        void publish(IntegrationEvent event);
    }

    public interface IntegrationAudit {
        void recordSuccess(String requestId, String externalDocumentId, int attempt);
        void recordRetry(String requestId, int attempt, String reason);
        void recordFailure(String requestId, int attempt, String reason);
    }

    public static final class ApprovedRequest {
        public final String requestId;
        public final String departmentCode;
        public final BigDecimal amount;
        public final String currency;
        public final String status;

        public ApprovedRequest(String requestId, String departmentCode, BigDecimal amount, String currency, String status) {
            this.requestId = Objects.requireNonNull(requestId, "requestId");
            this.departmentCode = Objects.requireNonNull(departmentCode, "departmentCode");
            this.amount = Objects.requireNonNull(amount, "amount");
            this.currency = Objects.requireNonNull(currency, "currency");
            this.status = Objects.requireNonNull(status, "status");
        }
    }

    public static final class FinanceTransferCommand {
        public final String idempotencyKey;
        public final String requestId;
        public final String departmentCode;
        public final BigDecimal amount;
        public final String currency;
        public final int attempt;
        public final Instant createdAt;

        private FinanceTransferCommand(String idempotencyKey, String requestId, String departmentCode, BigDecimal amount, String currency, int attempt, Instant createdAt) {
            this.idempotencyKey = idempotencyKey;
            this.requestId = requestId;
            this.departmentCode = departmentCode;
            this.amount = amount;
            this.currency = currency;
            this.attempt = attempt;
            this.createdAt = createdAt;
        }
    }

    public static final class FinanceGatewayResponse {
        public final String externalDocumentId;

        public FinanceGatewayResponse(String externalDocumentId) {
            this.externalDocumentId = Objects.requireNonNull(externalDocumentId, "externalDocumentId");
        }
    }

    public static final class FinanceTransferResult {
        public final boolean accepted;
        public final boolean retryExhausted;
        public final String externalDocumentId;
        public final String rejectionReason;
        public final List<AttemptLog> attempts;

        private FinanceTransferResult(boolean accepted, boolean retryExhausted, String externalDocumentId, String rejectionReason, List<AttemptLog> attempts) {
            this.accepted = accepted;
            this.retryExhausted = retryExhausted;
            this.externalDocumentId = externalDocumentId;
            this.rejectionReason = rejectionReason;
            this.attempts = Collections.unmodifiableList(new ArrayList<>(attempts));
        }

        public static FinanceTransferResult accepted(String externalDocumentId, List<AttemptLog> attempts) {
            return new FinanceTransferResult(true, false, externalDocumentId, null, attempts);
        }

        public static FinanceTransferResult rejected(String reason, List<AttemptLog> attempts) {
            return new FinanceTransferResult(false, false, null, reason, attempts);
        }

        public static FinanceTransferResult retryExhausted(List<AttemptLog> attempts) {
            return new FinanceTransferResult(false, true, null, "Retry attempts exhausted", attempts);
        }
    }

    public static final class AttemptLog {
        public final int attempt;
        public final String status;
        public final String message;
        public final Duration nextRetryAfter;

        private AttemptLog(int attempt, String status, String message, Duration nextRetryAfter) {
            this.attempt = attempt;
            this.status = status;
            this.message = message;
            this.nextRetryAfter = nextRetryAfter;
        }

        static AttemptLog success(int attempt, String externalDocumentId) {
            return new AttemptLog(attempt, "SUCCESS", externalDocumentId, Duration.ZERO);
        }

        static AttemptLog retry(int attempt, String message, Duration nextRetryAfter) {
            return new AttemptLog(attempt, "RETRY", message, nextRetryAfter);
        }

        static AttemptLog rejected(int attempt, String message) {
            return new AttemptLog(attempt, "REJECTED", message, Duration.ZERO);
        }
    }

    public static final class IntegrationEvent {
        public final String type;
        public final String requestId;
        public final String payload;
        public final Instant occurredAt;

        private IntegrationEvent(String type, String requestId, String payload, Instant occurredAt) {
            this.type = type;
            this.requestId = requestId;
            this.payload = payload;
            this.occurredAt = occurredAt;
        }

        static IntegrationEvent transferred(String requestId, String externalDocumentId) {
            return new IntegrationEvent("FINANCE_TRANSFERRED", requestId, externalDocumentId, Instant.now());
        }

        static IntegrationEvent failed(String requestId, String reason) {
            return new IntegrationEvent("FINANCE_TRANSFER_FAILED", requestId, reason, Instant.now());
        }
    }

    public static class TemporaryFinanceException extends RuntimeException {
        public TemporaryFinanceException(String message) {
            super(message);
        }
    }

    public static class PermanentFinanceException extends RuntimeException {
        public PermanentFinanceException(String message) {
            super(message);
        }
    }
}