package ru.master.exp.baseline;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Ручная реализация построения маршрута согласования.
 *
 * Маршрут зависит от суммы заявки, признака персональных данных,
 * связи с договором и ситуации, когда автор сам является руководителем.
 */
public final class ApprovalRouteBuilder {
    /**
     * Формирует список ролей, которые должны согласовать заявку.
     *
     * @param request заявка
     * @param authorIsDepartmentManager {@code true}, если автор является руководителем подразделения
     * @return упорядоченный список ролей согласующих
     */
    public List<Role> buildRoute(ApprovalRequest request, boolean authorIsDepartmentManager) {
        List<Role> route = new ArrayList<>();

        if (authorIsDepartmentManager) {
            route.add(Role.DIRECTOR);
        } else {
            route.add(Role.DEPARTMENT_MANAGER);
        }

        if (request.amount().compareTo(new BigDecimal("50000")) >= 0) {
            route.add(Role.FINANCE_OFFICER);
        }

        if (request.amount().compareTo(new BigDecimal("500000")) >= 0) {
            route.add(Role.DIRECTOR);
        }

        if (request.containsPersonalData()) {
            route.add(Role.SECURITY_OFFICER);
        }

        if (request.contractRelated()) {
            route.add(Role.LEGAL_OFFICER);
        }

        return List.copyOf(route);
    }
}
