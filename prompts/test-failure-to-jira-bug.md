# Prompt: test failure → Jira Bug (MCP or dry-run)

Use when `./gradlew apiTest`, `uiTest`, or `regressionTest` failed.

## Option A — Cursor + Atlassian MCP (preferred for real Jira)

Prerequisites: [docs/mcp-setup.md](../docs/mcp-setup.md), story key from `docs/test-cases.json` → `jiraKey`.

```
You are a QA engineer. A test failed in the TestProject repo.

1. Read the failure from Gradle output / build/reports/tests / (if present) Allure.
2. Map to docs/test-cases.json:
   - testCaseId from @DisplayName (LOCAL-SURVEY-TC-*)
   - jiraKey, acRef from matching case
3. Using Atlassian MCP, create a Bug in project {{JIRA_PROJECT_KEY}}:
   - Link to parent story {{JIRA_KEY}}
   - Summary: [TEST FAIL] {testCaseId}: {short reason}
   - Description sections:
     * Test: class#method
     * AC ref: acRef
     * Expected vs Actual
     * Last API response body (if API test; no auth headers)
     * Stack trace (trimmed)
   - Labels: ai-generated, automated-test
4. Do NOT paste secrets, tokens, or PII.
5. Reply with created issue key and URL.

Failure details:
---
{{PASTE_GRADLE_OR_REPORT_OUTPUT}}
---
```

## Option B — Java dry-run (CI / no MCP)

Default in this repo: `jira.dryRun=true`, `llm.stub=true` in Gradle.

```
Inspect helpers/ReportingPipelineTest and helpers/FailureContext.
After a failed test, build FailureContext manually or via TestFailureReportingExtension (when added):

- testName: fully qualified test method
- testCaseId: parsed from @DisplayName or UNKNOWN
- jiraKey: from docs/test-cases.json (e.g. LOCAL-SURVEY)
- message: assertion message
- lastApiResponse: Rest Assured body if applicable
- stackTrace: short stack

Run LlmBugReportGenerator → JiraIssuePublisher (logs JIRA_DRY_RUN JSON).

Copy JSON into Jira manually or re-run with Option A and paste JSON as context.
```

## Option C — Signal only (no Jira)

For local iteration: fix test first; use `./gradlew test` exit code + HTML report under `build/reports/tests/`.

Allure (when enabled): `./gradlew allureServe` for attachments (screenshots on UI fail).

## Checklist before filing

- [ ] Failure is not flaky environment (Playwright: see README macOS section)
- [ ] WireMock contract matches `docs/api-contract.md`
- [ ] UI fixture `survey.html` behavior matches AC
- [ ] Not a known POC gap (AC-1 one-time survey on profile create)
