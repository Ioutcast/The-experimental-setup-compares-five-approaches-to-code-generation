package ru.master.exp.baseline;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Заявка в ручном эталоне экспериментального приложения.
 *
 * Класс хранит минимальный набор полей, которые нужны для проверки
 * маршрутизации, прав доступа и переходов между состояниями.
 */
public final class ApprovalRequest {
    private final UUID id;
    private final String authorId;
    private final String departmentId;
    private final BigDecimal amount;
    private final boolean containsPersonalData;
    private final boolean contractRelated;
    private RequestStatus status;

    /**
     * Создает новую заявку в статусе {@link RequestStatus#DRAFT}.
     *
     * @param id идентификатор заявки
     * @param authorId идентификатор автора
     * @param departmentId идентификатор подразделения автора
     * @param amount сумма заявки
     * @param containsPersonalData признак наличия персональных данных
     * @param contractRelated признак связи заявки с договором
     */
    public ApprovalRequest(
            UUID id,
            String authorId,
            String departmentId,
            BigDecimal amount,
            boolean containsPersonalData,
            boolean contractRelated
    ) {
        this.id = Objects.requireNonNull(id);
        this.authorId = Objects.requireNonNull(authorId);
        this.departmentId = Objects.requireNonNull(departmentId);
        this.amount = Objects.requireNonNull(amount);
        this.containsPersonalData = containsPersonalData;
        this.contractRelated = contractRelated;
        this.status = RequestStatus.DRAFT;
    }

    /**
     * @return идентификатор заявки
     */
    public UUID id() {
        return id;
    }

    /**
     * @return идентификатор автора заявки
     */
    public String authorId() {
        return authorId;
    }

    /**
     * @return идентификатор подразделения автора
     */
    public String departmentId() {
        return departmentId;
    }

    /**
     * @return сумма заявки
     */
    public BigDecimal amount() {
        return amount;
    }

    /**
     * @return {@code true}, если заявка содержит персональные данные
     */
    public boolean containsPersonalData() {
        return containsPersonalData;
    }

    /**
     * @return {@code true}, если заявка связана с договором
     */
    public boolean contractRelated() {
        return contractRelated;
    }

    /**
     * @return текущий статус заявки
     */
    public RequestStatus status() {
        return status;
    }

    /**
     * Изменяет статус заявки.
     *
     * Метод имеет пакетную область видимости, чтобы смена статуса проходила
     * через {@link RequestStateMachine}, а не напрямую из внешнего кода.
     *
     * @param status новый статус
     */
    void changeStatus(RequestStatus status) {
        this.status = Objects.requireNonNull(status);
    }
}
