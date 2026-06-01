package org.example.ui.mock;

/**
 * Abstraction over browser DOM — mocked in tests, real driver can implement later.
 */
public interface SurveyView {
    boolean isSurveyDisplayed();

    boolean isSkipVisible();

    int getSelectedGenreCount();

    void selectGenres(int count);

    boolean isNextButtonPresent();

    boolean isNextButtonEnabledInDom();
}
