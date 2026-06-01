package org.example.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.support.ApiConfig;

import static io.restassured.RestAssured.given;

public class ProfileApiClient {
    private final String baseUrl;

    public ProfileApiClient() {
        this(ApiConfig.baseUrl());
    }

    public ProfileApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response savePreferences(String profileId, VodPreferencesPayload payload) {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/v1/profile/{profileId}/vod-preferences", profileId);
    }

    public Response getRecommendations(String profileId) {
        return given()
                .baseUri(baseUrl)
                .when()
                .get("/v1/profile/{profileId}/recommendations", profileId);
    }
}
