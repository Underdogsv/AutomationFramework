package ui.tests;

import common.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ui.pom.MoviesStepPage;
import ui.pom.SurveyStepPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag(Constants.Tags.UI)
@Tag(Constants.Tags.REGRESSION)
class SurveyFlowUiTest extends BaseUiTest {

    @Test
    @Tag(Constants.Tags.SMOKE)
    @DisplayName("AC-2: Next disabled on initial load (no genres selected)")
    void nextIsDisabledOnInitialLoad() {
        SurveyStepPage survey = openSurvey();
        assertEquals(0, survey.selectedGenreCount());
        assertFalse(survey.isNextEnabled());
    }

    @Test
    @Tag(Constants.Tags.SMOKE)
    @Description("testCaseId: LOCAL-SURVEY-TC-UI1")
    @Story("AC-2")
    @DisplayName("LOCAL-SURVEY-TC-UI1 / AC-2: Next disabled when only 2 genres selected")
    void nextIsDisabledWhenOnlyTwoGenresSelected() {
        SurveyStepPage survey = openSurvey();
        survey.selectGenres(2);
        assertEquals(2, survey.selectedGenreCount());
        assertFalse(survey.isNextEnabled());
    }

    @Test
    @Tag(Constants.Tags.SMOKE)
    @Description("testCaseId: LOCAL-SURVEY-TC-UI1")
    @Story("AC-2")
    @DisplayName("LOCAL-SURVEY-TC-UI1 / AC-2: Next enabled when 3 genres selected")
    void nextIsEnabledWhenThreeGenresSelected() {
        SurveyStepPage survey = openSurvey();
        survey.selectGenres(Constants.Survey.MIN_GENRES_FOR_NEXT);
        assertEquals(3, survey.selectedGenreCount());
        assertTrue(survey.isNextEnabled());
    }

    @Test
    @DisplayName("AC-2: Next enabled when more than 3 genres selected")
    void nextIsEnabledWhenFourGenresSelected() {
        SurveyStepPage survey = openSurvey();
        survey.selectGenres(4);
        assertTrue(survey.isNextEnabled());
    }

    @Test
    @DisplayName("AC-2: Next disabled again after unchecking below minimum")
    void nextIsDisabledAfterUncheckingBelowMinimum() {
        SurveyStepPage survey = openSurvey();
        survey.selectGenres(3);
        assertTrue(survey.isNextEnabled());
        survey.uncheckGenre(0);
        assertEquals(2, survey.selectedGenreCount());
        assertFalse(survey.isNextEnabled());
    }

    @Test
    @Tag(Constants.Tags.SMOKE)
    @Description("testCaseId: LOCAL-SURVEY-TC-UI3")
    @Story("AC-4")
    @DisplayName("AC-4: Skip control visible and dismisses survey (default path; API in api.tests)")
    void skipDismissesSurvey() {
        SurveyStepPage survey = openSurvey();
        assertTrue(survey.isSkipVisible());
        survey.clickSkip();
        assertFalse(survey.isVisible());
        assertTrue(survey.isSkippedScreenVisible());
    }

    @Test
    @Description("testCaseId: LOCAL-SURVEY-TC-UI2")
    @Story("AC-3")
    @DisplayName("AC-3: Movies step shown after 3 genres and Next")
    void moviesStepAppearsAfterGenresAndNext() {
        SurveyStepPage survey = openSurvey();
        survey.selectGenres(Constants.Survey.MIN_GENRES_FOR_NEXT);
        survey.clickNext();

        assertFalse(survey.isVisible());
        assertTrue(moviesStep().isVisible());
    }

    @Test
    @Description("testCaseId: LOCAL-SURVEY-TC-UI2")
    @Story("AC-3")
    @DisplayName("AC-3: Movie list filtered by selected genres")
    void moviesAreFilteredBySelectedGenres() {
        // selectGenreByIndexes (not selectGenres(3)): need 1+2+3 vs 1+2+4 to prove filtering, not always first three boxes
        SurveyStepPage survey = openSurvey();
        survey.selectGenreByIndexes(0, 1, 2);
        survey.clickNext();
        assertEquals(6, moviesStep().visibleMovieCount());

        SurveyStepPage surveyWithSciFi = openSurvey();
        surveyWithSciFi.selectGenreByIndexes(0, 1, 3);
        surveyWithSciFi.clickNext();
        assertEquals(7, moviesStep().visibleMovieCount());
    }

    @Test
    @Description("testCaseId: LOCAL-SURVEY-TC-UI2")
    @Story("AC-3")
    @DisplayName("AC-3: User can select exactly 5 movies; Finish enabled")
    void userCanSelectFiveMoviesAndFinish() {
        SurveyStepPage survey = openSurvey();
        survey.selectGenres(Constants.Survey.MIN_GENRES_FOR_NEXT);
        survey.clickNext();

        MoviesStepPage movies = moviesStep();
        assertFalse(movies.isFinishEnabled());
        movies.selectMovies(Constants.Survey.EXPECTED_MOVIES_ON_STEP_TWO);
        assertEquals(Constants.Survey.EXPECTED_MOVIES_ON_STEP_TWO, movies.selectedMovieCount());
        assertTrue(movies.isFinishEnabled());
    }
}
