package api.services;

import api.RestClient;
import api.models.VodPreferencesPayload;
import common.Constants;
import io.restassured.response.Response;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProfileApiService {

    private final RestClient restClient;

    public ProfileApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Response createProfile() {
        return restClient.post(Constants.ApiPaths.CREATE_PROFILE, Map.of());
    }

    public Response getOnboardingStatus(String profileId) {
        return restClient.get(Constants.ApiPaths.ONBOARDING_STATUS, profileId);
    }

    public Response savePreferences(String profileId, VodPreferencesPayload payload) {
        return restClient.post(Constants.ApiPaths.VOD_PREFERENCES, payload, profileId);
    }

    public Response getPreferences(String profileId) {
        return restClient.get(Constants.ApiPaths.VOD_PREFERENCES, profileId);
    }

    public Response getRecommendations(String profileId) {
        return restClient.get(Constants.ApiPaths.RECOMMENDATIONS, profileId);
    }
}
