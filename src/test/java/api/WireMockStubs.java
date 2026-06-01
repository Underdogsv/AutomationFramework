package api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import common.Constants;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class WireMockStubs {
    private static final String ONBOARDING_SCENARIO = "SurveyOnboarding";
    private static final String SURVEY_COMPLETED = "SurveyCompleted";

    private WireMockStubs() {
    }

    public static void configure(WireMockServer server) {
        configureProfileOnboarding(server);

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

        server.stubFor(get(urlPathMatching("/v1/profile/profile-personalized/vod-preferences"))
                .willReturn(aResponse()
                        .withStatus(Constants.Http.OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"profileId\":\"profile-personalized\",\"saved\":true,\"genreIds\":[1,2,3],\"movieIds\":[101,102,103,104,105]}")));

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

    private static void configureProfileOnboarding(WireMockServer server) {
        String profileId = Constants.Profiles.NEW_CREATED;
        String onboardingPath = "/v1/profile/" + profileId + "/onboarding-status";
        String preferencesPath = "/v1/profile/" + profileId + "/vod-preferences";

        server.stubFor(post(urlPathEqualTo(Constants.ApiPaths.CREATE_PROFILE))
                .willReturn(aResponse()
                        .withStatus(Constants.Http.OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"profileId\":\"" + profileId + "\",\"surveyRequired\":true}")));

        server.stubFor(get(urlPathEqualTo(onboardingPath))
                .inScenario(ONBOARDING_SCENARIO)
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(aResponse()
                        .withStatus(Constants.Http.OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"profileId\":\"" + profileId + "\",\"surveyRequired\":true}")));

        server.stubFor(get(urlPathEqualTo(onboardingPath))
                .inScenario(ONBOARDING_SCENARIO)
                .whenScenarioStateIs(SURVEY_COMPLETED)
                .willReturn(aResponse()
                        .withStatus(Constants.Http.OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"profileId\":\"" + profileId + "\",\"surveyRequired\":false}")));

        server.stubFor(post(urlPathEqualTo(preferencesPath))
                .inScenario(ONBOARDING_SCENARIO)
                .whenScenarioStateIs(Scenario.STARTED)
                .atPriority(3)
                .withRequestBody(matchingJsonPath("$.skipped", equalTo("true")))
                .willSetStateTo(SURVEY_COMPLETED)
                .willReturn(aResponse()
                        .withStatus(Constants.Http.OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"profileId\":\"" + profileId + "\",\"saved\":true,\"skipped\":true}")));

        server.stubFor(post(urlPathEqualTo(preferencesPath))
                .inScenario(ONBOARDING_SCENARIO)
                .whenScenarioStateIs(Scenario.STARTED)
                .atPriority(3)
                .withRequestBody(matchingJsonPath("$.genreIds[2]"))
                .willSetStateTo(SURVEY_COMPLETED)
                .willReturn(aResponse()
                        .withStatus(Constants.Http.OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"profileId\":\"" + profileId + "\",\"saved\":true,\"skipped\":false}")));
    }
}
