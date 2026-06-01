package org.example.ui.mock;

import org.example.ui.pom.SurveyPageRules;

public class MockSurveyView implements SurveyView {
    private boolean surveyDisplayed = true;
    private boolean skipVisible = true;
    private int selectedGenreCount;
    private boolean nextPresent = true;

    @Override
    public boolean isSurveyDisplayed() {
        return surveyDisplayed;
    }

    public void setSurveyDisplayed(boolean surveyDisplayed) {
        this.surveyDisplayed = surveyDisplayed;
    }

    @Override
    public boolean isSkipVisible() {
        return skipVisible && surveyDisplayed;
    }

    @Override
    public int getSelectedGenreCount() {
        return selectedGenreCount;
    }

    @Override
    public void selectGenres(int count) {
        this.selectedGenreCount = Math.max(0, count);
    }

    @Override
    public boolean isNextButtonPresent() {
        return nextPresent && surveyDisplayed;
    }

    public void setNextButtonPresent(boolean nextPresent) {
        this.nextPresent = nextPresent;
    }

    @Override
    public boolean isNextButtonEnabledInDom() {
        return isNextButtonPresent()
                && selectedGenreCount >= SurveyPageRules.MIN_GENRES_FOR_NEXT;
    }
}
