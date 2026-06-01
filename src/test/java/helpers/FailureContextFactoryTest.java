package helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FailureContextFactoryTest {

    @Test
    void extractTestCaseId_findsIdInDisplayName() {
        assertEquals(
                "LOCAL-SURVEY-TC-UI1",
                FailureContextFactory.extractTestCaseId(
                        "LOCAL-SURVEY-TC-UI1 / AC-2: Next enabled when 3 genres selected"));
    }

    @Test
    void extractTestCaseId_returnsUnknownWhenMissing() {
        assertEquals("UNKNOWN", FailureContextFactory.extractTestCaseId("AC-2: some test"));
    }
}
