# UI layer

Packages: `ui.tests`, `ui.pom`, `ui.driver`, `ui.config`, `ui.UiTestConfig`.

UI tests **do not** load WireMock or `ProfileApiService` — they use [`UiTestConfig`](../src/test/java/ui/UiTestConfig.java) only. Mixed API+UI flows belong in `e2e.tests.BaseE2ETest` (not used yet in this POC).

## Base classes

| Class | Spring context | Responsibility |
|-------|----------------|----------------|
| `AbstractPlaywrightSupport` | (none) | `@BeforeEach` / `@AfterEach` browser lifecycle, `openSurvey()`, `moviesStep()` |
| `BaseUiTest` | `UiTestConfig` | Allure `@Feature("ui")`, `UiAllureScreenshotExtension`, `@Tag(ui)` |
| `BaseE2ETest` | `E2eTestConfig` | API + Playwright when a test needs both |

`PlaywrightFactory` uses a **ThreadLocal** stack per JUnit worker thread — safe with parallel UI methods.

## Parallelism

- `@Execution(CONCURRENT)` on `BaseUiTest`
- `src/test/resources/junit-platform.properties` — parallel classes/methods
- `./gradlew uiTest` (no `maxParallelForks=1`)

## Writing a UI test

Each test navigates explicitly (no global `openSurvey()` in `@BeforeEach`):

```java
@Test
void nextIsEnabledWhenThreeGenresSelected() {
    SurveyStepPage survey = openSurvey();
    survey.selectGenres(Constants.Survey.MIN_GENRES_FOR_NEXT);
    assertTrue(survey.isNextEnabled());
}
```

## Test class

| Class | Scope |
|-------|--------|
| `ui.tests.SurveyFlowUiTest` | AC-2, AC-3, AC-4 — flat `@Test` methods, English `@DisplayName` with `LOCAL-SURVEY-TC-*` |

Prefer flat `@Test` methods over `@Nested` — step names live in `@DisplayName`.

## Scope (in / out)

| In scope | Out of scope |
|----------|----------------|
| Next / Finish **enabled** or **disabled** | Colors, fonts, alignment, pixel layout |
| Step visibility (genre vs movies) | Real production app shell |
| Movie list **count** filtered by genres | Full E2E with live backend |
| Skip dismisses survey fixture | |

API validates skip → default recommendations and saved preferences; see `api.tests` and [api-contract.md](api-contract.md).

## Run

```bash
./gradlew playwrightInstall uiTest
./gradlew uiTest -Dui.headless=false   # debug headed
```
