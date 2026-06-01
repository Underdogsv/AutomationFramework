package api;

import com.github.tomakehurst.wiremock.WireMockServer;
import common.Constants;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class WireMockStubs {
    private WireMockStubs() {
    }

    public static void configure(WireMockServer server) {
        server.stubFor(post(urlPathMatching("/v1/profile/profile-unknown/vod-preferences"))
                .willReturn(aResponse()
                        .withStatus(Constants.Http.NOT_FOUND)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"PROFILE_NOT_FOUND\"}")));

        server.stubFor(post(urlPathMatching("/v1/profile/.*/vod-preferences"))
                .atPriority(5)
                .withRequestBody(matchingJsonPath("$.skipped", equalTo("true")))
                .willReturn(aResponse()
                        .withStatus(Constants.Http.OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"profileId\":\"profile-1\",\"saved\":true,\"skipped\":true}")));

        server.stubFor(post(urlPathMatching("/v1/profile/.*/vod-preferences"))
                .atPriority(10)
                .withRequestBody(matchingJsonPath("$.genreIds[2]"))
                .willReturn(aResponse()
                        .withStatus(Constants.Http.OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"profileId\":\"profile-1\",\"saved\":true,\"skipped\":false}")));

        server.stubFor(post(urlPathMatching("/v1/profile/.*/vod-preferences"))
                .atPriority(20)
                .willReturn(aResponse()
                        .withStatus(Constants.Http.BAD_REQUEST)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"MIN_GENRES_REQUIRED\",\"message\":\"At least 3 genres required\"}")));

        server.stubFor(get(urlPathMatching("/v1/profile/profile-default/recommendations"))
                .willReturn(aResponse()
                        .withStatus(Constants.Http.OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"source\":\"default\",\"itemIds\":[1,2,3,4,5]}")));

        server.stubFor(get(urlPathMatching("/v1/profile/profile-personalized/recommendations"))
                .willReturn(aResponse()
                        .withStatus(Constants.Http.OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"source\":\"personalized\",\"itemIds\":[101,102,103,104,105]}")));

        server.stubFor(get(urlPathMatching("/v1/profile/.*/recommendations"))
                .atPriority(10)
                .willReturn(aResponse()
                        .withStatus(Constants.Http.OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"source\":\"default\",\"itemIds\":[1,2,3,4,5]}")));
    }
}
