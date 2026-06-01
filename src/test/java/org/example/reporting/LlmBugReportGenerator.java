package org.example.reporting;

import java.util.List;

/**
 * Generates bug report text from failure context.
 * AI-FIX: stub mode for CI; replace with OpenAI HTTP client when OPENAI_API_KEY is set.
 */
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
                    "Test case: " + context.testCaseId() + "\nJira: " + context.jiraKey()
                            + "\n\n" + context.message(),
                    List.of(
                            "Run failing test: " + context.testName(),
                            "Compare API response with expected contract",
                            "See docs/api-contract.md"
                    ),
                    "Per acceptance criteria in " + context.jiraKey(),
                    context.lastApiResponse() != null ? context.lastApiResponse() : context.message()
            );
        }
        throw new UnsupportedOperationException("Live LLM not configured; set llm.stub=true");
    }
}
