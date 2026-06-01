package utils;

import java.net.URL;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

public final class FixtureUtils {
    private FixtureUtils() {
    }

    public static String surveyFixtureUrl() {
        try {
            URL resource = Objects.requireNonNull(
                    FixtureUtils.class.getClassLoader().getResource("fixtures/survey.html"),
                    "fixtures/survey.html not on classpath");
            return Paths.get(resource.toURI()).toUri().toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid survey fixture path", e);
        }
    }
}
