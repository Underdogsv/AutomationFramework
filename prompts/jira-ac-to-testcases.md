# Prompt: Jira AC → test cases

Use with **Atlassian Rovo MCP** in Cursor ([mcp-setup.md](../docs/mcp-setup.md)).

## Prompt template

```
You are a senior QA engineer. Read Jira issue {{JIRA_KEY}} via MCP.

1. Extract Acceptance Criteria (AC field, Description, subtasks).
2. Generate 15–20 regression cases in TestRail-compatible JSON for docs/test-cases.json.

Each case must include:
- id (e.g. LOCAL-SURVEY-TC-API-01)
- title
- priority (P0/P1/P2)
- type: positive | negative | boundary | e2e
- layer: API | UI | E2E
- preconditions
- steps (array)
- expectedResult (string)
- automationCandidate: true/false
- acRef (e.g. AC-2)
- notes (optional: target test method name)

Rules:
- API-first; mock via docs/api-contract.md paths
- UI: business logic only (enabled/disabled, steps) — NO colors, layout, fonts, alignment
- Cover: survey once (AC-1), min 3 genres, 5 movies, skip→default, save exact IDs, recommendations change

3. Cross-check field names: genreIds, movieIds (not "genres").
4. Merge into docs/test-cases.json with human review.
5. List gaps vs AC.
```

## Fallback

If MCP unavailable: paste User Story, set `jiraKey: LOCAL-SURVEY`, `source: docx-fallback`.

Workflow: [docs/jira-mcp-workflow.md](../docs/jira-mcp-workflow.md).
