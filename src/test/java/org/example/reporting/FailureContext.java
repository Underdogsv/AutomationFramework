package org.example.reporting;

public record FailureContext(
        String testName,
        String testCaseId,
        String jiraKey,
        String message,
        String lastApiResponse,
        String stackTrace
) {
}
