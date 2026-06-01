# AI + Jira on test failure (Stage 3)

## Architecture

```
JUnit test failed
    → TestFailureReportingExtension (api.tests / ui.tests via BaseApiTest)
    → FailureContextFactory
    → LlmBugReportGenerator (stub by default)
    → JiraIssuePublisher (dry-run logs JSON)
```

Optional: use [prompts/test-failure-to-jira-bug.md](../prompts/test-failure-to-jira-bug.md) with Atlassian MCP to create a real Bug in Jira.

## Java module (`helpers`)

| Class | Role |
|-------|------|
| `FailureContext` | testName, testCaseId, jiraKey, message, API body, stack |
| `FailureContextFactory` | Builds context from JUnit `ExtensionContext` |
| `TestFailureReportingExtension` | `TestWatcher` on failed tests |
| `LlmBugReportGenerator` | Stub (`llm.stub=true`) or future OpenAI HTTP |
| `JiraIssuePublisher` | Dry-run logs JSON; live publish not implemented in POC |
| `ReportingPipelineTest` | Manual demo of the pipeline (`@Tag reporting`) |

## Configuration (Gradle / CLI)

| Property | Default | Meaning |
|----------|---------|---------|
| `reporting.onFailure` | `true` | Call pipeline on test failure |
| `jira.dryRun` | `true` | Log JSON instead of HTTP POST |
| `llm.stub` | `true` | Stub bug summary |
| `jira.key` | `LOCAL-SURVEY` | Epic / story key in reports |
| `jira.projectKey` | `QA` | Target Jira project |

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
