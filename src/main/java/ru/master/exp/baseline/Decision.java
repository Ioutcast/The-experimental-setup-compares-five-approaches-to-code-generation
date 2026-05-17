package ru.master.exp.baseline;

/**
 * Решение, которое может принять согласующий на шаге маршрута.
 */
public enum Decision {
    /** Согласовать заявку. */
    APPROVE,
    /** Отклонить заявку. */
    REJECT,
    /** Вернуть заявку автору на доработку. */
    RETURN_TO_REVISION
}
