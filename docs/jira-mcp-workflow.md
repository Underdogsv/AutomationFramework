# Jira → test cases (optional, not configured)

No Jira instance in this repo. Cases use **`LOCAL-SURVEY`** in `docs/test-cases.json`.

When you have Atlassian Cloud:

1. Follow [mcp-setup.md](mcp-setup.md).
2. Run `prompts/jira-ac-to-testcases.md` against your story key.
3. Run `prompts/jira-ac-to-java-tests.md` to generate/update Java tests.
4. On failure: `prompts/test-failure-to-jira-bug.md`.
