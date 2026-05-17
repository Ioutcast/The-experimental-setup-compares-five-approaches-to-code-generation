package ru.master.exp.baseline;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Ручная реализация конечного автомата состояний заявки.
 *
 * Класс задает допустимые переходы между статусами и используется как
 * эталон для сравнения с автоматически сгенерированным workflow-кодом.
 */
public final class RequestStateMachine {
    private final Map<RequestStatus, Set<RequestStatus>> transitions = new EnumMap<>(RequestStatus.class);

    /**
     * Инициализирует таблицу допустимых переходов.
     */
    public RequestStateMachine() {
        transitions.put(RequestStatus.DRAFT, EnumSet.of(RequestStatus.SUBMITTED, RequestStatus.CANCELLED));
        transitions.put(RequestStatus.SUBMITTED, EnumSet.of(RequestStatus.ROUTING));
        transitions.put(RequestStatus.ROUTING, EnumSet.of(RequestStatus.IN_APPROVAL));
        transitions.put(RequestStatus.IN_APPROVAL, EnumSet.of(
                RequestStatus.APPROVED,
                RequestStatus.REJECTED,
                RequestStatus.REVISION_REQUIRED
        ));
        transitions.put(RequestStatus.REVISION_REQUIRED, EnumSet.of(RequestStatus.SUBMITTED, RequestStatus.CANCELLED));
        transitions.put(RequestStatus.APPROVED, EnumSet.of(RequestStatus.EXECUTION_PENDING, RequestStatus.CLOSED));
        transitions.put(RequestStatus.EXECUTION_PENDING, EnumSet.of(RequestStatus.EXECUTED));
        transitions.put(RequestStatus.EXECUTED, EnumSet.of(RequestStatus.CLOSED));
    }

    /**
     * Проверяет, разрешен ли переход между двумя статусами.
     *
     * @param from исходный статус
     * @param to целевой статус
     * @return {@code true}, если переход разрешен
     */
    public boolean canTransit(RequestStatus from, RequestStatus to) {
        return transitions.getOrDefault(from, EnumSet.noneOf(RequestStatus.class)).contains(to);
    }

    /**
     * Выполняет переход заявки в новый статус.
     *
     * @param request заявка
     * @param targetStatus целевой статус
     * @throws IllegalStateException если переход запрещен таблицей состояний
     */
    public void transit(ApprovalRequest request, RequestStatus targetStatus) {
        if (!canTransit(request.status(), targetStatus)) {
            throw new IllegalStateException("Transition is not allowed: " + request.status() + " -> " + targetStatus);
        }
        request.changeStatus(targetStatus);
    }
}
