# PROMPTS.md — AI-Driven Automation Challenge

Log of **prompts**, **review decisions**, and **corrections** for the LOCAL-SURVEY POC.  
Detailed mistake write-ups: [AI mistakes and reviewer corrections](#ai-mistakes-and-reviewer-corrections).

---

## Workflow (3 stages)

| Stage | Goal | Prompt / artifact |
|-------|------|-------------------|
| 1 | Test cases from User Story | [§1](#1-test-cases-stage-1) → `docs/test-cases.json` |
| 2 | Automated tests (API + UI) | [§2](#2-api-tests-stage-2) · [§3](#3-ui-tests-stage-2) |
| 3 | Failure → Jira (dry-run) | [§4](#4-failure--jira-stage-3) → `prompts/test-failure-to-jira-bug.md` |

Jira MCP for AC import: `prompts/jira-ac-to-testcases.md`, `docs/mcp-setup.md`.

---

## 1. Test cases (Stage 1)

```
Generate regression test cases for "Interest survey for new profile".
Cover: one-time survey, min 3 genres for Next, 5 movies on step 2, skip → default recs,
POST /v1/profile/{id}/vod-preferences, recommendations affected by saved data.
Output: TestRail-style JSON (id, acRef, type, layer, preconditions, steps, expectedResult, automationCandidate).
NO visual/layout cases. API-first where possible.
```

**Output:** `docs/test-cases.json` (19 regression cases).

---

## 2. API tests (Stage 2)

```
Generate JUnit 5 + REST Assured tests against WireMock (api.baseUrl from Spring).
Use docs/api-contract.md and LOCAL-SURVEY-TC-* in @DisplayName.
Extend BaseApiTest. Tag: api.
Verify request bodies and saved state where AC requires exact genreIds/movieIds — not only HTTP 200.
```

**Also used:** `prompts/jira-ac-to-java-tests.md` when generating from Jira AC.

---

## 3. UI tests (Stage 2)

```
Playwright + POM (SurveyStepPage, MoviesStepPage) on fixtures/survey.html.
Business logic only: Next enabled/disabled by genre count, 5 movies on step 2, skip dismisses survey.
Extend BaseUiTest. English @DisplayName with AC + test case ids. Flat @Test methods, no @Nested.
No colors, layout, fonts, or alignment checks.
```

---

## 4. Failure → Jira (Stage 3)

```
LlmBugReportGenerator (stub) + JiraIssuePublisher (dry-run).
TestFailureReportingExtension on API/UI base tests. Parse testCaseId from @DisplayName.
```

Full MCP flow: `prompts/test-failure-to-jira-bug.md`.

---

## AI mistakes and reviewer corrections

Role: **Prompt Engineer** (clear constraints, contract-first) and **Reviewer** (reject weak asserts, iterate until AC-proof).

### 1. Visual UI checks (color, layout)

| | |
|---|---|
| **Original AI output** | Tests for button color, alignment, font size, block position. |
| **Why incorrect** | Out of AC scope; does not prove “≥3 genres → Next enabled”. Fragile and environment-dependent. |
| **Correction prompt** | *Validate only enabled/disabled, step visibility, skip, Finish after 5 movies — no CSS/layout.* |
| **Final decision** | `SurveyFlowUiTest`: `isNextEnabled()`, `isFinishEnabled()`, counts — no visual asserts. Test-case JSON excludes layout cases. |

*Related fixes:* removed `@Nested` genre groups (flat tests); replaced mock-only UI with Playwright + `survey.html`.

### 2. Only 3 movies on step 2

| | |
|---|---|
| **Original AI output** | Movies step test with 3 selections or vague “select some movies”. |
| **Why incorrect** | AC-3 requires **5** movies before finish (`Constants.Survey.EXPECTED_MOVIES_ON_STEP_TWO`). |
| **Correction prompt** | *Finish disabled until exactly 5 selected; assert selectedMovieCount == 5.* |
| **Final decision** | `userCanSelectFiveMoviesAndFinish()`; case `LOCAL-SURVEY-TC-UI-10`. |

### 3. Preferences save: only HTTP 200 / `saved=true`

| | |
|---|---|
| **Original AI output** | `statusCode(200)` + `saved=true` only. |
| **Why incorrect** | AC-5: must persist **exact** `genreIds` and `movieIds`, not just success flag. |
| **Correction prompt** | *WireMock verify equalToJson on POST body; GET vod-preferences returns same IDs.* |
| **Final decision** | `VodPreferencesApiTest.savePreferences_withThreeGenres_returns200()` — `verify(equalToJson(...))` + `getPreferences()` per-index asserts. Contract in `docs/api-contract.md`. |

*Related fixes:* field name `genreIds` (not `genres`); dynamic WireMock port (not hardcoded `8089`); 400 for **&lt;3** genres, not “exactly 3 only”.

### 4. Personalized recommendations without proof

| | |
|---|---|
| **Original AI output** | GET on `profile-personalized` → `source=personalized` with no prior save or default baseline. |
| **Why incorrect** | AC-6: recommendations must **change** after preferences; static stub does not prove causality. |
| **Correction prompt** | *Show default after skip vs personalized after save; stateful onboarding for survey-once.* |
| **Final decision** | Skip → `source=default`; save chain → `e2e_savePreferences_thenPersonalizedRecommendations()`; `ProfileOnboardingApiTest` + WireMock **scenarios** for `surveyRequired` once. Case `LOCAL-SURVEY-TC-P2` / skip path in JSON. |

*Related fixes:* skip → default recs covered in API (`RecommendationsApiTest`); UI skip only dismisses fixture (AC-4 UI), recs validated at API layer.

### Reviewer checklist

1. Map tests to AC + `LOCAL-SURVEY-TC-*` (`@DisplayName`, `docs/test-cases.json`).
2. No visual/layout asserts in UI prompts.
3. Exact payload / saved IDs where AC says “save selected …”.
4. Stateful mocks or WireMock verify — not static happy-path only.
5. Layer bases: `BaseApiTest` / `BaseUiTest` / `BaseE2ETest` — no unnecessary WireMock in pure UI.

---

## Metrics (estimate)

| | Share |
|---|--------|
| Test / stub code from AI prompts | ~75% |
| Manual review & wiring (WireMock priorities, contracts, base classes) | ~25% |
