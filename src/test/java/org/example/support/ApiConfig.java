package org.example.support;

public final class ApiConfig {
    private ApiConfig() {
    }

    public static String baseUrl() {
        return System.getProperty("api.baseUrl", "http://localhost:8089");
    }
}
