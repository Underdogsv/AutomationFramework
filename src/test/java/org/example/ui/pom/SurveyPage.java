package org.example.ui.pom;

import org.example.ui.mock.SurveyView;

/**
 * Page Object for interest survey — business rules only (mock-friendly).
 * <p>
 * Conditional UI (documented, not layout-tested):
 * <ul>
 *   <li>{@link SurveyLocators#SURVEY_OVERLAY} — visible only before survey completed/skipped</li>
 *   <li>{@link SurveyLocators#NEXT_BUTTON} — enabled when selected genres &gt;= {@link SurveyPageRules#MIN_GENRES_FOR_NEXT}</li>
 *   <li>{@link SurveyLocators#SKIP_BUTTON} — visible while survey is shown</li>
 * </ul>
 */
public class SurveyPage {
    private final SurveyView view;

    public SurveyPage(SurveyView view) {
        this.view = view;
    }

    public boolean shouldShowSurvey() {
        return view.isSurveyDisplayed();
    }

    public boolean canProceedToMoviesStep() {
        return view.isNextButtonPresent() && view.getSelectedGenreCount() >= SurveyPageRules.MIN_GENRES_FOR_NEXT;
    }

    public boolean isNextButtonEnabled() {
        return canProceedToMoviesStep() && view.isNextButtonEnabledInDom();
    }

    public boolean isSkipAvailable() {
        return view.isSurveyDisplayed() && view.isSkipVisible();
    }

    public void selectGenres(int count) {
        view.selectGenres(count);
    }
}
