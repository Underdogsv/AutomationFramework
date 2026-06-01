package api.tests;

import api.models.VodPreferencesPayload;
import api.services.ProfileApiService;
import common.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.*;

@Tag(Constants.Tags.REGRESSION)
class RecommendationsApiTest extends BaseApiTest {

    @Autowired
    private ProfileApiService profileApi;

    @Test
    @Tag(Constants.Tags.SMOKE)
    @Description("testCaseId: LOCAL-SURVEY-TC-P1")
    @Story("AC-6")
    @DisplayName("LOCAL-SURVEY-TC-P1: personalized recommendations")
    void getRecommendations_personalizedProfile_returnsPersonalizedSource() {
        profileApi.getRecommendations(Constants.Profiles.PERSONALIZED)
                .then()
                .statusCode(Constants.Http.OK)
                .body("source", equalTo("personalized"))
                .body("itemIds", hasItems(101, 102, 103, 104, 105));
    }

    @Test
    @Tag(Constants.Tags.SMOKE)
    @Description("testCaseId: LOCAL-SURVEY-TC-P2")
    @Story("AC-4, AC-6")
    @DisplayName("LOCAL-SURVEY-TC-P2: default recommendations after skip")
    void getRecommendations_afterSkip_returnsDefaultSource() {
        profileApi.savePreferences(Constants.Profiles.DEFAULT, VodPreferencesPayload.skip())
                .then()
                .statusCode(Constants.Http.OK);

        profileApi.getRecommendations(Constants.Profiles.DEFAULT)
                .then()
                .statusCode(Constants.Http.OK)
                .body("source", equalTo("default"));
    }

    @Test
    void e2e_savePreferences_thenPersonalizedRecommendations() {
        VodPreferencesPayload payload = VodPreferencesPayload.withGenresAndMovies(
                List.of(10, 20, 30),
                List.of(101, 102, 103, 104, 105));

        profileApi.savePreferences(Constants.Profiles.PERSONALIZED, payload)
                .then()
                .statusCode(Constants.Http.OK);

        profileApi.getRecommendations(Constants.Profiles.PERSONALIZED)
                .then()
                .statusCode(Constants.Http.OK)
                .body("source", equalTo("personalized"))
                .body("itemIds", hasSize(5));
    }
}
