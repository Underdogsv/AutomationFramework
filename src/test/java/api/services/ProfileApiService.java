package api.services;

import api.RestClient;
import api.models.VodPreferencesPayload;
import common.Constants;
import io.restassured.response.Response;
import org.springframework.stereotype.Service;

@Service
public class ProfileApiService {

    private final RestClient restClient;

    public ProfileApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Response savePreferences(String profileId, VodPreferencesPayload payload) {
        return restClient.post(Constants.ApiPaths.VOD_PREFERENCES, payload, profileId);
    }

    public Response getRecommendations(String profileId) {
        return restClient.get(Constants.ApiPaths.RECOMMENDATIONS, profileId);
    }
}
