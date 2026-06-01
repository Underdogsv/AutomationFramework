package org.example.reporting;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("reporting")
class ReportingPipelineTest {
    @Test
    void dryRunPublish_logsValidJiraPayload() throws Exception {
        var context = new FailureContext(
                "savePreferences_withTwoGenres_returns400",
                "LOCAL-SURVEY-TC-N1",
                "LOCAL-SURVEY",
                "Expected 400 but was 200",
                "{\"error\":\"MIN_GENRES_REQUIRED\"}",
                "AssertionError at line 42"
        );

        var report = new LlmBugReportGenerator(true).generate(context);
        assertTrue(report.summary().contains("savePreferences"));

        var publisher = new JiraIssuePublisher(true);
        String result = publisher.publish(report, "QA");
        assertEquals("DRY-RUN", result);
    }
}
