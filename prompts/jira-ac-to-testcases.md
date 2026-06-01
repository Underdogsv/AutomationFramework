# Prompt: Jira AC → test cases

Use with **Atlassian Rovo MCP** connected in Cursor.

## Prompt template

```
You are a senior QA engineer. Read Jira issue {{JIRA_KEY}} via MCP.

1. Extract Acceptance Criteria from: AC field, Description, subtasks.
2. Generate regression test cases in JSON for TMS import.
3. Rules from assignment:
   - Priority: API end-to-end; NO UI layout/color/position checks.
   - UI only for business logic (e.g. button enabled/disabled).
   - Types: positive, negative, boundary.
   - Each case: id, acRef, title, type, layer (api|ui-logic), priority, steps, expected.
   - Link jiraKey: {{JIRA_KEY}}.

4. Cross-check API paths with docs/api-contract.md in this repo.
5. Write output to docs/test-cases.json (merge or replace with approval).

6. List gaps you added manually vs AC (if any).
```

## Example (this repo fallback)

If MCP unavailable, paste User Story from docx and set `jiraKey: LOCAL-SURVEY`.
