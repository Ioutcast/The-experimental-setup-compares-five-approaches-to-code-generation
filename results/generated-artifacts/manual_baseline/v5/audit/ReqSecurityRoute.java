package generated.workflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Requirement: Добавление согласования службы безопасности для заявок с персональными данными. Coverage estimate: 0.85. */
public final class ReqSecurityRoute {
    public List<String> build(BigDecimal amount, boolean containsPersonalData, boolean contractRelated) {
        List<String> steps = new ArrayList<>();
        steps.add("DEPARTMENT_MANAGER");
        if (amount.compareTo(new BigDecimal("50000")) >= 0) steps.add("FINANCE_OFFICER");
        if (amount.compareTo(new BigDecimal("500000")) >= 0) steps.add("DIRECTOR");
        if (containsPersonalData) steps.add("SECURITY_OFFICER");
        if (contractRelated) steps.add("LEGAL_OFFICER");
        return List.copyOf(steps);
    }
}