package api;

import configs.ApiProperties;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

@Component
public class RequestSpecProvider {

    private final ApiProperties props;

    public RequestSpecProvider(ApiProperties props) {
        this.props = props;
    }

    public RequestSpecification build() {
        return new RequestSpecBuilder()
                .setBaseUri(props.getBaseUrl())
                .setContentType(ContentType.JSON)
                .build();
    }
}
