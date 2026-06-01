# Prompt: test cases + AC → Java tests

Use after [`jira-ac-to-testcases.md`](jira-ac-to-testcases.md) (or when `docs/test-cases.json` already exists).

## Prerequisites

- Read `docs/test-cases.json`, `docs/api-contract.md`, `docs/ui-layer.md`
- API base URL comes from WireMock via Spring (`configs.ApiProperties`), not hardcoded ports
- Do **not** use `var` for locals; use explicit types

## Prompt template

```
You are a senior QA automation engineer working in this Java repo (mock POC).

Inputs:
- docs/test-cases.json (cases with id, acRef, layer, steps, expected, notes)
- docs/api-contract.md
- Existing packages: api.tests, api.services, api.models, api.WireMockStubs, ui.tests, ui.pom, helpers

Task:
1. For each case where layer is "api":
   - Add or update a test method in api.tests (VodPreferencesApiTest or RecommendationsApiTest, or new class if needed).
   - Extend api.tests.BaseApiTest; @Autowired ProfileApiService; no manual new ProfileApiClient().
   - @Tag(Constants.Tags.API); add SMOKE/REGRESSION per case priority.
   - @DisplayName in English: include case id (e.g. LOCAL-SURVEY-TC-P1) and acRef (e.g. AC-2).

2. For each case where layer is "ui":
   - Add or update methods in ui.tests.SurveyFlowUiTest (flat @Test methods, no @Nested).
   - Extend ui.tests.BaseUiTest; call openSurvey() in each test; use ui.pom page objects.
   - @Tag(Constants.Tags.UI); English @DisplayName with case id and acRef.
   - NO layout/color/position assertions; business logic only (enabled/disabled, step visibility).

3. WireMock:
   - If a case needs a new stub, update api/WireMockStubs.java to match docs/api-contract.md.
   - Keep stub priorities for skip vs min-3-genres vs 400.

4. Traceability:
   - Update "notes" in docs/test-cases.json with exact test method names.
   - List any AC from acRef not covered and why (e.g. AC-1 needs real profile E2E).

5. Conventions:
   - Constants from common.Constants (Http, Tags, Profiles, Survey, ApiPaths).
   - Run mentally: ./gradlew apiTest && ./gradlew uiTest

Output: changed Java files + updated docs/test-cases.json notes. Summarize gaps.
```

## Package map (do not recreate com.teamrotator.*)

| Layer | Package | Base class |
|-------|---------|------------|
| API tests | `api.tests` | `BaseApiTest` |
| API client | `api.services` | `ProfileApiService` |
| Payload | `api.models` | `VodPreferencesPayload` |
| UI tests | `ui.tests` | `BaseUiTest` |
| POM | `ui.pom` | `SurveyStepPage`, `MoviesStepPage` |

## Anti-patterns (reject if AI suggests)

- `org.example.*` or `com.teamrotator.qa.*` packages
- Playwright locators inside test methods (use POM)
- `@Nested` UI groups without strong reason
- Hardcoded `http://localhost:8089`
- UI tests for pixel/CSS/layout
