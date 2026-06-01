package org.example.api;

import org.example.support.BaseApiTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;

@Tag("api")
class RecommendationsApiTest extends BaseApiTest {
    private ProfileApiClient client;

    @BeforeEach
    void setUp() {
        client = new ProfileApiClient();
    }

    @Test
    void getRecommendations_afterPersonalizedProfile_returnsPersonalizedSource() {
        client.getRecommendations("profile-personalized")
                .then()
                .statusCode(200)
                .body("source", equalTo("personalized"))
                .body("itemIds", hasItems(101, 102, 103, 104, 105));
    }

    @Test
    void getRecommendations_afterSkip_returnsDefaultSource() {
        client.savePreferences("profile-default", VodPreferencesPayload.skip())
                .then()
                .statusCode(200);

        client.getRecommendations("profile-default")
                .then()
                .statusCode(200)
                .body("source", equalTo("default"));
    }

    @Test
    void e2e_savePreferences_thenGetPersonalizedRecommendations() {
        var payload = VodPreferencesPayload.withGenresAndMovies(
                List.of(10, 20, 30),
                List.of(101, 102, 103, 104, 105));

        client.savePreferences("profile-personalized", payload).then().statusCode(200);

        client.getRecommendations("profile-personalized")
                .then()
                .statusCode(200)
                .body("source", equalTo("personalized"))
                .body("itemIds", hasSize(5));
    }
}
