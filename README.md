# TestProdject — AI-Driven Automation Challenge

**Fully mocked prototype** — no production API, no Jira, no browser UI.

## Stack

- Java 17, Gradle, JUnit 5
- **API:** REST Assured + WireMock
- **UI:** POM + `MockSurveyView` (in-memory)
- **Reporting:** LLM stub + Jira dry-run (logs JSON only)

## Quick start

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
./gradlew test
```

## Layout

| Path | Role |
|------|------|
| `docs/api-contract.md` | Mock API (preferences + recommendations) |
| `docs/test-cases.json` | Cases (`LOCAL-SURVEY`) |
| `docs/ui-mock-approach.md` | Mock POM approach |
| `docs/jira-mcp-workflow.md` | Optional Jira MCP (later) |
| `docs/ai-jira-integration.md` | Failure → bug dry-run |
| `src/test/java/.../api/` | API tests |
| `src/test/java/.../ui/` | Mock UI tests |
| `src/test/java/.../reporting/` | Reporting pipeline |
| `PROMPTS.md`, `AI-STRATEGY.md` | Assignment docs |

## Deliverables

- [x] Java tests (API + mock UI)
- [x] PROMPTS.md, AI-STRATEGY.md, test-cases.json
- [x] Stage 3 reporting (stub/dry-run)
