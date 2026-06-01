package e2e.tests;

import api.models.VodPreferencesPayload;
import api.services.ProfileApiService;
import com.github.tomakehurst.wiremock.WireMockServer;
import common.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import ui.pom.MoviesStepPage;
import ui.pom.SurveyStepPage;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * E2E: Playwright exercises the survey fixture; Rest Assured validates WireMock API state.
 * The HTML fixture does not call the backend — after the UI path, the test submits the same
 * preferences via API (as the real client would) and asserts recommendations changed.
 */
@Tag(Constants.Tags.REGRESSION)
@Execution(ExecutionMode.SAME_THREAD)
class SurveyE2ETest extends BaseE2ETest {

    private static final List<Integer> GENRE_IDS = List.of(1, 2, 3);
    private static final List<Integer> MOVIE_IDS = List.of(101, 102, 103, 104, 105);

    @Autowired
    private ProfileApiService profileApi;

    @Autowired
    private WireMockServer wireMockServer;

    @BeforeEach
    void resetWireMockScenarios() {
        wireMockServer.resetScenarios();
    }

    @Test
    @Tag(Constants.Tags.SMOKE)
    @Description("testCaseId: LOCAL-SURVEY-TC-E2E-01")
    @Story("AC-3, AC-5, AC-6")
    @DisplayName("LOCAL-SURVEY-TC-E2E-01: UI survey completion then API personalized recommendations")
    void completeSurveyInBrowser_thenApiReflectsPersonalizedRecommendations() {
        String profileId = Constants.Profiles.RECOMMENDATION_SCENARIO;
        VodPreferencesPayload payload = VodPreferencesPayload.withGenresAndMovies(GENRE_IDS, MOVIE_IDS);

        profileApi.getRecommendations(profileId)
                .then()
                .statusCode(Constants.Http.OK)
                .body("source", equalTo("default"))
                .body("itemIds", hasItems(1, 2, 3, 4, 5));

        SurveyStepPage survey = openSurvey();
        survey.selectGenres(Constants.Survey.MIN_GENRES_FOR_NEXT);
        assertEquals(Constants.Survey.MIN_GENRES_FOR_NEXT, survey.selectedGenreCount());
        assertTrue(survey.isNextEnabled());
        survey.clickNext();

        MoviesStepPage movies = moviesStep();
        assertTrue(movies.isVisible());
        movies.selectMovies(Constants.Survey.EXPECTED_MOVIES_ON_STEP_TWO);
        assertEquals(Constants.Survey.EXPECTED_MOVIES_ON_STEP_TWO, movies.selectedMovieCount());
        assertTrue(movies.isFinishEnabled());

        profileApi.savePreferences(profileId, payload)
                .then()
                .statusCode(Constants.Http.OK)
                .body("saved", equalTo(true));

        profileApi.getRecommendations(profileId)
                .then()
                .statusCode(Constants.Http.OK)
                .body("source", equalTo("personalized"))
                .body("itemIds", hasItems(101, 102, 103, 104, 105))
                .body("basedOnGenreIds", hasItems(1, 2, 3));
    }

    @Test
    @Tag(Constants.Tags.SMOKE)
    @Description("testCaseId: LOCAL-SURVEY-TC-E2E-02")
    @Story("AC-4, AC-6")
    @DisplayName("LOCAL-SURVEY-TC-E2E-02: UI skip then API default recommendations")
    void skipSurveyInBrowser_thenApiReturnsDefaultRecommendations() {
        String profileId = Constants.Profiles.DEFAULT;

        SurveyStepPage survey = openSurvey();
        assertTrue(survey.isSkipVisible());
        survey.clickSkip();
        assertTrue(survey.isSkippedScreenVisible());

        profileApi.savePreferences(profileId, VodPreferencesPayload.skip())
                .then()
                .statusCode(Constants.Http.OK)
                .body("skipped", equalTo(true));

        profileApi.getRecommendations(profileId)
                .then()
                .statusCode(Constants.Http.OK)
                .body("source", equalTo("default"));
    }
}
