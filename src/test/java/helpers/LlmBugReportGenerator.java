package helpers;

import java.util.List;

public class LlmBugReportGenerator {
    private final boolean stub;

    public LlmBugReportGenerator() {
        this(Boolean.parseBoolean(System.getProperty("llm.stub", "true")));
    }

    public LlmBugReportGenerator(boolean stub) {
        this.stub = stub;
    }

    public BugReportDto generate(FailureContext context) {
        if (stub) {
            return new BugReportDto(
                    "[AI] " + context.testName() + " failed",
                    "Test case: " + context.testCaseId() + "\nJira: " + context.jiraKey() + "\n\n" + context.message(),
                    List.of("Run: " + context.testName(), "See docs/api-contract.md"),
                    "Per AC in " + context.jiraKey(),
                    context.lastApiResponse() != null ? context.lastApiResponse() : context.message()
            );
        }
        throw new UnsupportedOperationException("Live LLM not configured");
    }
}
