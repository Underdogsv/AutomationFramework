# Jira → test cases (optional, not configured)

No Jira instance in this repo. Cases use **`LOCAL-SURVEY`** in `docs/test-cases.json`.

When you have Atlassian Cloud:

1. Add Atlassian Rovo MCP to Cursor (`mcp.json` — see Atlassian docs).
2. Connect via **Settings → Tools & MCP**.
3. Run `prompts/jira-ac-to-testcases.md` against your story key.
4. Update `docs/test-cases.json` with real `jiraKey`.
