package api;

import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class RestClient {

    private final RequestSpecProvider requestSpecProvider;

    public RestClient(RequestSpecProvider requestSpecProvider) {
        this.requestSpecProvider = requestSpecProvider;
    }

    public Response get(String path, Object... pathParams) {
        return given()
                .spec(requestSpecProvider.build())
                .when()
                .get(path, pathParams);
    }

    public Response post(String path, Object body, Object... pathParams) {
        return given()
                .spec(requestSpecProvider.build())
                .body(body)
                .when()
                .post(path, pathParams);
    }
}
