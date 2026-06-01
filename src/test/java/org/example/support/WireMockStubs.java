package org.example.support;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class WireMockStubs {
    private WireMockStubs() {
    }

    public static void configure() {
        stubFor(post(urlPathMatching("/v1/profile/profile-unknown/vod-preferences"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"PROFILE_NOT_FOUND\"}")));

        stubFor(post(urlPathMatching("/v1/profile/.*/vod-preferences"))
                .atPriority(5)
                .withRequestBody(matchingJsonPath("$.skipped", equalTo("true")))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"profileId\":\"profile-1\",\"saved\":true,\"skipped\":true}")));

        stubFor(post(urlPathMatching("/v1/profile/.*/vod-preferences"))
                .atPriority(10)
                .withRequestBody(matchingJsonPath("$.genreIds[2]"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"profileId\":\"profile-1\",\"saved\":true,\"skipped\":false}")));

        stubFor(post(urlPathMatching("/v1/profile/.*/vod-preferences"))
                .atPriority(20)
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"MIN_GENRES_REQUIRED\",\"message\":\"At least 3 genres required\"}")));

        stubFor(get(urlPathMatching("/v1/profile/profile-default/recommendations"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"source\":\"default\",\"itemIds\":[1,2,3,4,5]}")));

        stubFor(get(urlPathMatching("/v1/profile/profile-personalized/recommendations"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"source\":\"personalized\",\"itemIds\":[101,102,103,104,105]}")));

        stubFor(get(urlPathMatching("/v1/profile/.*/recommendations"))
                .atPriority(10)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"source\":\"default\",\"itemIds\":[1,2,3,4,5]}")));
    }
}
