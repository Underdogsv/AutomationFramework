package configs;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.stereotype.Component;

@Component
public class ApiProperties {

    private final WireMockServer wireMockServer;

    public ApiProperties(WireMockServer wireMockServer) {
        this.wireMockServer = wireMockServer;
    }

    public String getBaseUrl() {
        return wireMockServer.baseUrl();
    }
}
