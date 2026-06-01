# PROMPTS.md — AI-Driven Automation Challenge

## 1. Test cases from User Story (Stage 1)

**Prompt (initial):**
```
Generate regression test cases for "Interest survey for new profile" feature.
Requirements: one-time survey, min 3 genres for Next, 5 movies step 2, skip → default recs,
POST /v1/profile/{id}/vod-preferences, recommendations endpoint affected.
Output: JSON for TestRail with id, acRef, type (positive/negative/boundary), layer, steps, expected.
NO UI layout/color tests. API first.
```

**AI mistakes & fixes:**
| Issue | Fix |
|-------|-----|
| Used `genres` instead of `genreIds` | Aligned with `docs/api-contract.md` |
| Missed skip → default path | Added LOCAL-SURVEY-TC-P2 manually |
| UI cases for button position | Replaced with ui-logic enabled/disabled only |

## 2. Jira MCP → test cases

See `prompts/jira-ac-to-testcases.md`. Log: use MCP `get_issue` when sandbox available.

## 3. API tests (Stage 2)

**Prompt:**
```
Generate JUnit 5 + REST Assured tests against WireMock base URL from system property api.baseUrl.
Use docs/api-contract.md and docs/test-cases.json IDs in @DisplayName.
Extend BaseApiTest. Tags: api.
```

**AI mistakes & fixes:**
| Issue | Fix |
|-------|-----|
| Hardcoded localhost:8089 | `BaseApiTest` sets dynamic WireMock port |
| Confused "exactly 3" vs "min 3" | Assert 400 only for 2 genres |

## 4. UI mock tests (POM + MockSurveyView)

**Prompt:**
```
Create Page Object for survey with conditional elements: overlay (one-time), next enabled if genres>=3, skip while survey visible.
Implement MockSurveyView and JUnit tests without Playwright or HTML fixtures.
```

**AI-FIX:** Removed Playwright and survey.html; tests assert POM rules only.

## 5. Failure → Jira (Stage 3)

**Prompt:**
```
Java LlmBugReportGenerator stub + JiraIssuePublisher dry-run.
Build Jira REST v3 issue JSON from BugReportDto.
```

## Metrics (estimate)

- ~75% test code from AI prompts
- ~25% manual: WireMock priorities, BaseApiTest, contract definition

