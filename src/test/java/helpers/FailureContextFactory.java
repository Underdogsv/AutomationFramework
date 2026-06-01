package helpers;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FailureContextFactory {
    private static final Pattern TEST_CASE_ID = Pattern.compile("LOCAL-SURVEY-TC-[A-Z0-9]+");
    private static final int MAX_STACK_CHARS = 2_000;

    private FailureContextFactory() {
    }

    public static FailureContext from(ExtensionContext context, Throwable cause) {
        String displayName = context.getDisplayName();
        String testCaseId = extractTestCaseId(displayName);
        String testName = context.getRequiredTestClass().getSimpleName()
                + "#"
                + context.getRequiredTestMethod().getName();
        String message = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getSimpleName();
        String stack = stackTraceSummary(cause);
        String jiraKey = System.getProperty("jira.key", "LOCAL-SURVEY");
        return new FailureContext(testName, testCaseId, jiraKey, message, null, stack);
    }

    static String extractTestCaseId(String displayName) {
        Matcher matcher = TEST_CASE_ID.matcher(displayName);
        if (matcher.find()) {
            return matcher.group();
        }
        return "UNKNOWN";
    }

    private static String stackTraceSummary(Throwable cause) {
        StringWriter writer = new StringWriter();
        cause.printStackTrace(new PrintWriter(writer));
        String stack = writer.toString();
        if (stack.length() <= MAX_STACK_CHARS) {
            return stack;
        }
        return stack.substring(0, MAX_STACK_CHARS) + "\n... (truncated)";
    }
}
