package ru.master.exp.baseline;

import java.util.Set;

/**
 * Ручная реализация правил доступа к заявке.
 *
 * Класс фиксирует ожидаемое поведение RBAC/ABAC-проверок для автора,
 * руководителя подразделения, согласующего и администратора.
 */
public final class AccessPolicy {
    /**
     * Проверяет, может ли пользователь редактировать черновик заявки.
     *
     * @param request заявка
     * @param actorId идентификатор пользователя
     * @return {@code true}, если пользователь является автором черновика
     */
    public boolean canEditDraft(ApprovalRequest request, String actorId) {
        return request.status() == RequestStatus.DRAFT && request.authorId().equals(actorId);
    }

    /**
     * Проверяет право просмотра заявки.
     *
     * Автор видит свою заявку, а руководитель подразделения видит заявки
     * своего подразделения.
     *
     * @param request заявка
     * @param actorId идентификатор пользователя
     * @param actorDepartmentId подразделение пользователя
     * @param actorRoles роли пользователя
     * @return {@code true}, если просмотр разрешен
     */
    public boolean canRead(
            ApprovalRequest request,
            String actorId,
            String actorDepartmentId,
            Set<Role> actorRoles
    ) {
        if (request.authorId().equals(actorId)) {
            return true;
        }
        return actorRoles.contains(Role.DEPARTMENT_MANAGER)
                && request.departmentId().equals(actorDepartmentId);
    }

    /**
     * Проверяет право согласования заявки.
     *
     * Пользователь должен иметь требуемую роль и не должен быть автором заявки.
     *
     * @param request заявка
     * @param actorId идентификатор пользователя
     * @param actorRoles роли пользователя
     * @param requiredRole роль, требуемая на шаге согласования
     * @return {@code true}, если согласование разрешено
     */
    public boolean canApprove(ApprovalRequest request, String actorId, Set<Role> actorRoles, Role requiredRole) {
        return !request.authorId().equals(actorId) && actorRoles.contains(requiredRole);
    }

    /**
     * Проверяет наличие административной роли.
     *
     * @param actorRoles роли пользователя
     * @return {@code true}, если пользователь является администратором
     */
    public boolean canAdministrate(Set<Role> actorRoles) {
        return actorRoles.contains(Role.ADMIN);
    }
}
