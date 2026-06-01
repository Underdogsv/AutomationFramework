package org.example.api;

import org.example.support.BaseApiTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;

@Tag("api")
class VodPreferencesApiTest extends BaseApiTest {
    private ProfileApiClient client;

    @BeforeEach
    void setUp() {
        client = new ProfileApiClient();
    }

    @Test
    void savePreferences_withThreeGenres_returns200() {
        var payload = VodPreferencesPayload.withGenresAndMovies(
                List.of(1, 2, 3),
                List.of(101, 102, 103, 104, 105));

        client.savePreferences("profile-personalized", payload)
                .then()
                .statusCode(200)
                .body("saved", equalTo(true))
                .body("skipped", equalTo(false));
    }

    @Test
    void savePreferences_withTwoGenres_returns400() {
        var payload = VodPreferencesPayload.withGenresAndMovies(
                List.of(1, 2),
                List.of());

        client.savePreferences("profile-1", payload)
                .then()
                .statusCode(400)
                .body("error", equalTo("MIN_GENRES_REQUIRED"));
    }

    @Test
    void savePreferences_unknownProfile_returns404() {
        var payload = VodPreferencesPayload.withGenresAndMovies(
                List.of(1, 2, 3),
                List.of(101, 102, 103, 104, 105));

        client.savePreferences("profile-unknown", payload)
                .then()
                .statusCode(404)
                .body("error", equalTo("PROFILE_NOT_FOUND"));
    }

    @Test
    void skipPreferences_returns200() {
        client.savePreferences("profile-default", VodPreferencesPayload.skip())
                .then()
                .statusCode(200)
                .body("skipped", equalTo(true));
    }
}
