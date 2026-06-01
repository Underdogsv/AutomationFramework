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

import static org.hamcrest.Matchers.equalTo;

/**
 * AC-1: Survey is required only once immediately after a new profile is created (mock API flow).
 */
@Tag(Constants.Tags.REGRESSION)
@Execution(ExecutionMode.SAME_THREAD)
class ProfileOnboardingApiTest extends BaseApiTest {

    @Autowired
    private ProfileApiService profileApi;

    @Autowired
    private WireMockServer wireMockServer;

    @BeforeEach
    void resetOnboardingScenario() {
        wireMockServer.resetScenarios();
    }

    @Test
    @Tag(Constants.Tags.SMOKE)
    @Description("testCaseId: LOCAL-SURVEY-TC-AC1")
    @Story("AC-1")
    @DisplayName("LOCAL-SURVEY-TC-AC1: survey required only once after new profile creation")
    void createNewProfile_shouldShowSurveyOnlyOnce() {
        String profileId = profileApi.createProfile()
                .then()
                .statusCode(Constants.Http.OK)
                .body("surveyRequired", equalTo(true))
                .extract()
                .path("profileId");

        profileApi.getOnboardingStatus(profileId)
                .then()
                .statusCode(Constants.Http.OK)
                .body("surveyRequired", equalTo(true));

        VodPreferencesPayload payload = VodPreferencesPayload.withGenresAndMovies(
                List.of(1, 2, 3),
                List.of(101, 102, 103, 104, 105));

        profileApi.savePreferences(profileId, payload)
                .then()
                .statusCode(Constants.Http.OK)
                .body("saved", equalTo(true));

        //repeated check — AC-1: survey not shown again
        assertSurveyNotRequired(profileId);
        assertSurveyNotRequired(profileId);
    }

    void assertSurveyNotRequired(String profileId) {
        profileApi.getOnboardingStatus(profileId)
                .then()
                .statusCode(Constants.Http.OK)
                .body("surveyRequired", equalTo(false));
    }
}
