package api.tests;

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

import java.util.List;

import static org.hamcrest.Matchers.*;

@Tag(Constants.Tags.REGRESSION)
@Execution(ExecutionMode.SAME_THREAD)
class RecommendationsApiTest extends BaseApiTest {

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
    @Description("testCaseId: LOCAL-SURVEY-TC-API-04")
    @Story("AC-6")
    @DisplayName("AC-6: save preferences changes recommendations from default to personalized")
    void savePreferences_shouldChangeRecommendationsFromDefaultToPersonalized() {
        String profileId = Constants.Profiles.RECOMMENDATION_SCENARIO;
        VodPreferencesPayload payload = VodPreferencesPayload.withGenresAndMovies(
                List.of(1, 2, 3),
                List.of(101, 102, 103, 104, 105));

        profileApi.getRecommendations(profileId)
                .then()
                .statusCode(Constants.Http.OK)
                .body("source", equalTo("default"))
                .body("itemIds", hasItems(1, 2, 3, 4, 5));

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
    @Description("testCaseId: LOCAL-SURVEY-TC-API-02")
    @Story("AC-4, AC-6")
    @DisplayName("LOCAL-SURVEY-TC-API-02: default recommendations after skip")
    void getRecommendations_afterSkip_returnsDefaultSource() {
        profileApi.savePreferences(Constants.Profiles.DEFAULT, VodPreferencesPayload.skip())
                .then()
                .statusCode(Constants.Http.OK);

        profileApi.getRecommendations(Constants.Profiles.DEFAULT)
                .then()
                .statusCode(Constants.Http.OK)
                .body("source", equalTo("default"));
    }
}
