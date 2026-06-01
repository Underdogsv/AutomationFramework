package org.example.ui;

import org.example.ui.mock.MockSurveyView;
import org.example.ui.pom.SurveyPage;
import org.example.ui.pom.SurveyPageRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Mock UI tests: POM + {@link org.example.ui.mock.MockSurveyView}, no browser.
 * Maps to LOCAL-SURVEY-TC-UI1 — «Далі» enabled when &gt;= 3 genres.
 */
@Tag("ui-mock")
class SurveyNextButtonMockTest {
    private MockSurveyView view;
    private SurveyPage surveyPage;

    @BeforeEach
    void setUp() {
        view = new MockSurveyView();
        surveyPage = new SurveyPage(view);
    }

    @Test
    @DisplayName("LOCAL-SURVEY-TC-UI1: Next disabled when fewer than 3 genres")
    void nextDisabled_whenLessThanThreeGenres() {
        surveyPage.selectGenres(2);
        assertFalse(surveyPage.canProceedToMoviesStep());
        assertFalse(surveyPage.isNextButtonEnabled());
    }

    @Test
    @DisplayName("LOCAL-SURVEY-TC-UI1: Next enabled at exactly 3 genres (boundary)")
    void nextEnabled_whenExactlyThreeGenres() {
        surveyPage.selectGenres(SurveyPageRules.MIN_GENRES_FOR_NEXT);
        assertTrue(surveyPage.canProceedToMoviesStep());
        assertTrue(surveyPage.isNextButtonEnabled());
    }

    @Test
    @DisplayName("Next enabled when more than 3 genres selected")
    void nextEnabled_whenFourGenres() {
        surveyPage.selectGenres(4);
        assertTrue(surveyPage.isNextButtonEnabled());
    }

    @Test
    @DisplayName("Skip visible while survey is displayed")
    void skipAvailable_whenSurveyShown() {
        assertTrue(surveyPage.shouldShowSurvey());
        assertTrue(surveyPage.isSkipAvailable());
    }

    @Test
    @DisplayName("Survey hidden — no next, no skip (conditional elements)")
    void surveyHidden_noInteractiveElements() {
        view.setSurveyDisplayed(false);
        assertFalse(surveyPage.shouldShowSurvey());
        assertFalse(surveyPage.isSkipAvailable());
        assertFalse(surveyPage.canProceedToMoviesStep());
    }
}
