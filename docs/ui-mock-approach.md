# Mock UI tests (POM + conditional elements)

UI coverage without a browser or HTML fixtures:

1. **POM** (`ui/pom/SurveyPage`, `SurveyLocators`) — conditional elements and business rules.
2. **Mock view** (`ui/mock/MockSurveyView`) — in-memory state.
3. **JUnit** (`SurveyNextButtonMockTest`) — asserts POM logic.

## Conditional elements (in POM)

| Element | Condition |
|---------|-----------|
| `survey-overlay` | Once after profile creation |
| `#next` | Enabled when `genres >= 3` |
| `#skip` | Visible while survey is active |

## Future real UI

Implement `SurveyView` with a real driver; keep `SurveyPage` and tests unchanged.
