# Prompt: test cases + AC → Java tests

Use after [`jira-ac-to-testcases.md`](jira-ac-to-testcases.md) (or when `docs/test-cases.json` already exists).

## Prerequisites

- Read `docs/test-cases.json`, `docs/api-contract.md`, `docs/ui-layer.md`
- API base URL: `configs.ApiProperties` → WireMock (no hardcoded ports)
- Explicit types in Java (no `var`)

## Prompt template

```
You are a senior QA automation engineer in this Java mock POC.

Inputs:
- docs/test-cases.json (id, acRef, layer, steps, expectedResult, automationCandidate, notes)
- docs/api-contract.md

Task:
1. layer "API":
   - api.tests.* extending BaseApiTest; @Autowired ProfileApiService
   - ProfileOnboardingApiTest (AC-1), VodPreferencesApiTest (AC-5), RecommendationsApiTest (AC-4/6)
   - @Tag(Constants.Tags.API); SMOKE/REGRESSION per priority
   - @DisplayName: case id + AC; @Description("testCaseId: …")
   - AC-5: WireMock verify exact POST body (equalToJson) + GET vod-preferences for saved IDs — not only 200/saved=true

2. layer "UI":
   - ui.tests.SurveyFlowUiTest — flat @Test, extend BaseUiTest (NOT BaseApiTest)
   - openSurvey() per test; ui.pom only — no locators in tests
   - Business logic only: enabled/disabled, step visibility, 5 movies, skip
   - NO layout/color/position

3. layer "E2E":
   - e2e.tests extending BaseE2ETest when test needs API + browser in one class

4. WireMock:
   - Update api/WireMockStubs.java per contract; use scenarios for stateful flows (onboarding, default→personalized)

5. Traceability:
   - Update test-cases.json "notes" with method names
   - List uncovered AC with reason

Conventions: common.Constants; ./gradlew apiTest && ./gradlew uiTest
```

## Package map

| Layer | Package | Base class | Spring config |
|-------|---------|------------|---------------|
| API | `api.tests` | `BaseApiTest` | `ApiTestConfig` |
| UI | `ui.tests` | `BaseUiTest` | `UiTestConfig` |
| E2E | `e2e.tests` | `BaseE2ETest` | `E2eTestConfig` |
| Service | `api.services` | `ProfileApiService` | — |
| POM | `ui.pom` | `SurveyStepPage`, `MoviesStepPage` | — |

## Anti-patterns

- `com.teamrotator.qa.*` / `org.example.*`
- `BaseUiTest extends BaseApiTest` (outdated — UI must not load WireMock)
- Hardcoded `localhost:8089`
- UI layout/CSS asserts
- `@Nested` without strong reason
