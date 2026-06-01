package helpers;

import common.Constants;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag(Constants.Tags.REPORTING)
@Tag(Constants.Tags.REGRESSION)
@Tag(Constants.Tags.SMOKE)
@Execution(ExecutionMode.CONCURRENT)
class ReportingPipelineTest {
    @Test
    void dryRunPublish_logsValidJiraPayload() throws Exception {
        FailureContext context = new FailureContext(
                "savePreferences_withTwoGenres_returns400",
                "LOCAL-SURVEY-TC-N1",
                "LOCAL-SURVEY",
                "Expected 400",
                "{\"error\":\"MIN_GENRES_REQUIRED\"}",
                "stack"
        );
        BugReportDto report = new LlmBugReportGenerator(true).generate(context);
        assertTrue(report.summary().contains("savePreferences"));
        assertEquals("DRY-RUN", new JiraIssuePublisher(true).publish(report, "QA"));
    }
}
