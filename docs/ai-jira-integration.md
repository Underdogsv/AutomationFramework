# AI + Jira on test failure (Stage 3)

## Architecture

```
JUnit test failed (api.tests or ui.tests)
    → TestFailureReportingExtension
    → FailureContextFactory
    → LlmBugReportGenerator (stub by default)
    → JiraIssuePublisher (dry-run logs JSON)
```

Attached on:

- `api.tests.BaseApiTest` — API tests (Spring + WireMock)
- `ui.tests.BaseUiTest` — UI tests (Playwright only; no WireMock in context)

Optional: [prompts/test-failure-to-jira-bug.md](../prompts/test-failure-to-jira-bug.md) + Atlassian MCP to create a real Bug.

## Java module (`helpers`)

| Class | Role |
|-------|------|
| `FailureContext` | testName, testCaseId, jiraKey, message, API body, stack |
| `FailureContextFactory` | Builds context; parses `LOCAL-SURVEY-TC-*` from `@DisplayName` |
| `TestFailureReportingExtension` | `TestWatcher` on failed tests |
| `LlmBugReportGenerator` | Stub (`llm.stub=true`) or future OpenAI HTTP |
| `JiraIssuePublisher` | Dry-run logs JSON; live HTTP not implemented in POC |
| `FailureContextFactoryTest` | Unit tests for testCaseId parsing (runs in `./gradlew test`) |
| `ReportingPipelineTest` | Pipeline smoke (`@Tag reporting`, `regression`, `smoke`) |

Helper tests are **not** tagged `api` / `ui` — they run in the full `test` task and in `regressionTest` / `smokeTest` via `reporting` tag on `ReportingPipelineTest` only.

## Configuration (Gradle / CLI)

| Property | Default | Meaning |
|----------|---------|---------|
| `reporting.onFailure` | `true` | Call pipeline on test failure |
| `jira.dryRun` | `true` | Log JSON instead of HTTP POST |
| `llm.stub` | `true` | Stub bug summary |
| `jira.key` | `LOCAL-SURVEY` | Epic / story key in reports |
| `jira.projectKey` | `QA` | Target Jira project |

GitHub Actions sets `reporting.onFailure=false` to reduce log noise.

## Enable live Jira (future)

```bash
export JIRA_BASE_URL=https://your-domain.atlassian.net
export JIRA_EMAIL=you@example.com
export JIRA_API_TOKEN=...
./gradlew test -Djira.dryRun=false -Dllm.stub=true
```

Live `JiraIssuePublisher.publish()` requires implementation beyond this POC.

## Security

- Never send Authorization headers or PII to LLM.
- Defaults are safe for CI: dry-run + stub.
