# UI layer

Packages: `ui.tests`, `ui.pom`, `ui.driver`, `ui.config`. `BaseUiTest` extends `api.tests.BaseApiTest` (Spring).

## BaseUiTest

`PlaywrightFactory` (ThreadLocal) gives each parallel test thread its own browser. **No** `openSurvey()` in `@BeforeEach`. `@AfterEach` calls `pw.cleanup()`.

Parallelism: `@Execution(CONCURRENT)` on `BaseUiTest`, JUnit Platform `parallel.mode.default=concurrent`, `./gradlew uiTest` without `maxParallelForks=1`.

Each test navigates to the screen under test:

```java
SurveyStepPage survey = openSurvey();           // interest survey
// future: RegistrationPage reg = openRegistration();
```

## Test classes

| Class | Scope |
|-------|--------|
| `ui.tests.SurveyFlowUiTest` | Survey TZ scenarios (flat `@Test` methods) |
| `RegistrationFlowUiTest` | (future) registration |

Prefer one flow class with several methods over `@Nested` inner classes — nested groups add little when steps are already named in `@DisplayName`.
