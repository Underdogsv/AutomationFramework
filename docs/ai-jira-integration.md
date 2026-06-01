# AI + Jira on test failure (Stage 3)

## Architecture

```
JUnit failure → FailureContext → LlmBugReportGenerator → JiraIssuePublisher
                                                      ↘ dry-run (default)
                                                      ↘ POST /rest/api/3/issue
```

## Java module

`src/test/java/org/example/reporting/`

| Class | Role |
|-------|------|
| `FailureContext` | testName, testCaseId, jiraKey, message, API body |
| `LlmBugReportGenerator` | stub (`llm.stub=true`) or future OpenAI HTTP |
| `JiraIssuePublisher` | dry-run logs JSON; live needs env vars |

## MCP alternative (Cursor)

After local test failure, prompt:

```
Using Atlassian MCP, create a Bug in project QA linked to LOCAL-SURVEY.
Summary: [from FailureContext]
Steps: ...
Do not include tokens or PII.
```

Use MCP for dev; use Java dry-run for reproducible CI proof.

## Enable live Jira

```bash
export JIRA_BASE_URL=https://your-domain.atlassian.net
export JIRA_EMAIL=you@example.com
export JIRA_API_TOKEN=...
./gradlew test -Djira.dryRun=false -Dllm.stub=true
```

## Security

- Never send Authorization headers or PII to LLM.
- Default `jira.dryRun=true` in `build.gradle`.
