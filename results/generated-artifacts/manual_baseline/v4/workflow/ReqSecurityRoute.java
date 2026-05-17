package generated.workflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Requirement: Добавление согласования службы безопасности для заявок с персональными данными. Coverage: 0.86. */
public final class ReqSecurityRoute {
    public ApprovalRoute buildRoute(RouteRequest request) {
        Objects.requireNonNull(request, "request");
        Set<ApprovalStep> steps = new LinkedHashSet<>();
        steps.add(new ApprovalStep("DEPARTMENT_MANAGER", "Проверка руководителем подразделения", 1));
        if (request.amount.compareTo(new BigDecimal("50000")) >= 0) {
            steps.add(new ApprovalStep("FINANCE_OFFICER", "Финансовый контроль суммы", 2));
        }
        if (request.amount.compareTo(new BigDecimal("500000")) >= 0) {
            steps.add(new ApprovalStep("DIRECTOR", "Согласование директором", 3));
        }
        if (request.containsPersonalData) {
            steps.add(new ApprovalStep("SECURITY_OFFICER", "Проверка персональных данных", 4));
        }
        if (request.contractRelated) {
            steps.add(new ApprovalStep("LEGAL_OFFICER", "Юридическая проверка договора", 5));
        }
        return new ApprovalRoute(request.requestId, new ArrayList<>(steps));
    }

    public static final class RouteRequest {
        public final String requestId;
        public final BigDecimal amount;
        public final boolean containsPersonalData;
        public final boolean contractRelated;

        public RouteRequest(String requestId, BigDecimal amount, boolean containsPersonalData, boolean contractRelated) {
            this.requestId = Objects.requireNonNull(requestId, "requestId");
            this.amount = Objects.requireNonNull(amount, "amount");
            this.containsPersonalData = containsPersonalData;
            this.contractRelated = contractRelated;
        }
    }

    public static final class ApprovalRoute {
        public final String requestId;
        public final List<ApprovalStep> steps;

        private ApprovalRoute(String requestId, List<ApprovalStep> steps) {
            this.requestId = requestId;
            this.steps = Collections.unmodifiableList(steps);
        }
    }

    public static final class ApprovalStep {
        public final String role;
        public final String description;
        public final int order;

        public ApprovalStep(String role, String description, int order) {
            this.role = role;
            this.description = description;
            this.order = order;
        }

        public boolean equals(Object other) {
            return other instanceof ApprovalStep && role.equals(((ApprovalStep) other).role);
        }

        public int hashCode() {
            return role.hashCode();
        }
    }
}