package ru.master.exp.baseline;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Запись журнала аудита ручного эталона.
 *
 * Используется для фиксации бизнес-событий, изменений статусов и попыток
 * выполнить запрещенное действие.
 */
public final class AuditRecord {
    private final UUID id;
    private final UUID requestId;
    private final String actorId;
    private final String eventType;
    private final String details;
    private final Instant createdAt;

    /**
     * Создает запись аудита.
     *
     * @param id идентификатор записи аудита
     * @param requestId идентификатор заявки
     * @param actorId идентификатор пользователя
     * @param eventType тип события
     * @param details детали события
     * @param createdAt дата и время создания записи
     */
    public AuditRecord(UUID id, UUID requestId, String actorId, String eventType, String details, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.requestId = Objects.requireNonNull(requestId);
        this.actorId = Objects.requireNonNull(actorId);
        this.eventType = Objects.requireNonNull(eventType);
        this.details = Objects.requireNonNull(details);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    /**
     * @return идентификатор записи аудита
     */
    public UUID id() {
        return id;
    }

    /**
     * @return идентификатор заявки
     */
    public UUID requestId() {
        return requestId;
    }

    /**
     * @return идентификатор пользователя, выполнившего действие
     */
    public String actorId() {
        return actorId;
    }

    /**
     * @return тип события аудита
     */
    public String eventType() {
        return eventType;
    }

    /**
     * @return детали события
     */
    public String details() {
        return details;
    }

    /**
     * @return дата и время создания записи
     */
    public Instant createdAt() {
        return createdAt;
    }
}
