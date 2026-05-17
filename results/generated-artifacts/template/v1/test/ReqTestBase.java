package generated.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Requirement: Тесты доменных правил, workflow-переходов и запретов доступа. Coverage: 0.52. */
public final class ReqTestBaseTest {
    public ScenarioSuite approvalWorkflowSuite() {
        ScenarioSuite suite = new ScenarioSuite("approval-workflow-regression");
        suite.add("DRAFT request can be submitted when required fields are completed", "SUBMITTED");
        suite.add("Author cannot approve own request", "ACCESS_DENIED");
        suite.add("Expired approval step creates SLA escalation event", "ESCALATED");
        suite.add("Approved request is sent to finance integration once", "FINANCE_TRANSFERRED");
        return suite;
    }

    public static final class ScenarioSuite {
        public final String name;
        private final List<ScenarioAssertion> assertions = new ArrayList<>();

        public ScenarioSuite(String name) {
            this.name = Objects.requireNonNull(name, "name");
        }

        public void add(String scenario, String expectedOutcome) {
            assertions.add(new ScenarioAssertion(scenario, expectedOutcome));
        }

        public List<ScenarioAssertion> assertions() {
            return Collections.unmodifiableList(assertions);
        }
    }

    public static final class ScenarioAssertion {
        public final String scenario;
        public final String expectedOutcome;

        private ScenarioAssertion(String scenario, String expectedOutcome) {
            this.scenario = scenario;
            this.expectedOutcome = expectedOutcome;
        }
    }
}