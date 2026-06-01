package helpers;

import common.Constants;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * On test failure: FailureContext → LlmBugReportGenerator → JiraIssuePublisher (dry-run by default).
 * Disable with -Dreporting.onFailure=false. Skips tests tagged reporting.
 */
public class TestFailureReportingExtension implements TestWatcher {
    private static final Logger LOG = Logger.getLogger(TestFailureReportingExtension.class.getName());

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        if (!Boolean.parseBoolean(System.getProperty("reporting.onFailure", "true"))) {
            return;
        }
        if (context.getTags().contains(Constants.Tags.REPORTING)) {
            return;
        }
        try {
            FailureContext failureContext = FailureContextFactory.from(context, cause);
            BugReportDto report = new LlmBugReportGenerator().generate(failureContext);
            String projectKey = System.getProperty("jira.projectKey", "QA");
            String result = new JiraIssuePublisher().publish(report, projectKey);
            LOG.info("Failure reporting for " + failureContext.testName()
                    + " (testCaseId=" + failureContext.testCaseId() + "): " + result);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Failure reporting pipeline error (test failure still fails)", e);
        }
    }
}
