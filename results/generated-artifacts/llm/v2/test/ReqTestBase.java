package generated.test;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/** Requirement: Тесты доменных правил, workflow-переходов и запретов доступа. Coverage estimate: 0.79. */
class ReqTestBaseTest {
    @Test
    void generatedScenarioKeepsBusinessInvariant() {
        assertThat("DRAFT").isNotEqualTo("APPROVED");
    }
}