package generated.workflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Requirement: Построение маршрута по сумме заявки и обязательным ролям согласующих. Coverage estimate: 0.59. */
public final class ReqRoutingAmount {
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