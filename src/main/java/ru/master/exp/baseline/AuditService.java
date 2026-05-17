package ru.master.exp.baseline;

import java.time.Instant;
import java.util.UUID;

/**
 * Ручная реализация аудита действий с заявкой.
 *
 * Сервис создает записи аудита для бизнес-событий и запрещенных действий.
 */
public final class AuditService {
    /**
     * Создает запись аудита об изменении статуса заявки.
     *
     * @param request заявка
     * @param from исходный статус
     * @param to новый статус
     * @param actorId идентификатор пользователя, выполнившего действие
     * @return запись аудита
     */
    public AuditRecord statusChanged(ApprovalRequest request, RequestStatus from, RequestStatus to, String actorId) {
        return new AuditRecord(
                UUID.randomUUID(),
                request.id(),
                actorId,
                "STATUS_CHANGED",
                from + " -> " + to,
                Instant.now()
        );
    }

    /**
     * Создает запись аудита о запрещенной попытке действия.
     *
     * @param request заявка
     * @param actorId идентификатор пользователя
     * @param action название запрещенного действия
     * @return запись аудита с типом {@code SECURITY_EVENT}
     */
    public AuditRecord forbiddenAction(ApprovalRequest request, String actorId, String action) {
        return new AuditRecord(
                UUID.randomUUID(),
                request.id(),
                actorId,
                "SECURITY_EVENT",
                action,
                Instant.now()
        );
    }
}
