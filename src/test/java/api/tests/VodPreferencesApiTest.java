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

import static org.hamcrest.Matchers.equalTo;

@Tag(Constants.Tags.REGRESSION)
class VodPreferencesApiTest extends BaseApiTest {

    @Autowired
    private ProfileApiService profileApi;

    @Test
    @Tag(Constants.Tags.SMOKE)
    @Description("testCaseId: LOCAL-SURVEY-TC-P1")
    @Story("AC-2, AC-3, AC-5, AC-6")
    @DisplayName("LOCAL-SURVEY-TC-P1: save 3 genres and 5 movies")
    void savePreferences_withThreeGenres_returns200() {
        VodPreferencesPayload payload = VodPreferencesPayload.withGenresAndMovies(
                List.of(1, 2, 3),
                List.of(101, 102, 103, 104, 105));

        profileApi.savePreferences(Constants.Profiles.PERSONALIZED, payload)
                .then()
                .statusCode(Constants.Http.OK)
                .body("saved", equalTo(true))
                .body("skipped", equalTo(false));
    }

    @Test
    @Description("testCaseId: LOCAL-SURVEY-TC-N1")
    @Story("AC-2")
    @DisplayName("LOCAL-SURVEY-TC-N1: less than 3 genres returns 400")
    void savePreferences_withTwoGenres_returns400() {
        profileApi.savePreferences("profile-1", VodPreferencesPayload.withGenresAndMovies(List.of(1, 2), List.of()))
                .then()
                .statusCode(Constants.Http.BAD_REQUEST)
                .body("error", equalTo("MIN_GENRES_REQUIRED"));
    }

    @Test
    void savePreferences_unknownProfile_returns404() {
        VodPreferencesPayload payload = VodPreferencesPayload.withGenresAndMovies(
                List.of(1, 2, 3),
                List.of(101, 102, 103, 104, 105));

        profileApi.savePreferences(Constants.Profiles.UNKNOWN, payload)
                .then()
                .statusCode(Constants.Http.NOT_FOUND)
                .body("error", equalTo("PROFILE_NOT_FOUND"));
    }

    @Test
    @Tag(Constants.Tags.SMOKE)
    @Description("testCaseId: LOCAL-SURVEY-TC-P2")
    @Story("AC-4")
    @DisplayName("LOCAL-SURVEY-TC-P2: skip survey")
    void skipPreferences_returns200() {
        profileApi.savePreferences(Constants.Profiles.DEFAULT, VodPreferencesPayload.skip())
                .then()
                .statusCode(Constants.Http.OK)
                .body("skipped", equalTo(true));
    }
}
