package helpers;

public record FailureContext(
        String testName,
        String testCaseId,
        String jiraKey,
        String message,
        String lastApiResponse,
        String stackTrace
) {
}
