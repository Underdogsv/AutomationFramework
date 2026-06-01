package common;

public final class Constants {
    private Constants() {
    }

    public static final class Http {
        public static final int OK = 200;
        public static final int BAD_REQUEST = 400;
        public static final int NOT_FOUND = 404;

        private Http() {
        }
    }

    public static final class Tags {
        public static final String API = "api";
        public static final String UI = "ui";
        public static final String REPORTING = "reporting";
        public static final String SMOKE = "smoke";
        public static final String REGRESSION = "regression";
        public static final String E2E = "e2e";

        private Tags() {
        }
    }

    public static final class Survey {
        public static final int MIN_GENRES_FOR_NEXT = 3;
        public static final int EXPECTED_MOVIES_ON_STEP_TWO = 5;

        private Survey() {
        }
    }

    public static final class ApiPaths {
        public static final String CREATE_PROFILE = "/v1/profile";
        public static final String VOD_PREFERENCES = "/v1/profile/{profileId}/vod-preferences";
        public static final String ONBOARDING_STATUS = "/v1/profile/{profileId}/onboarding-status";
        public static final String RECOMMENDATIONS = "/v1/profile/{profileId}/recommendations";

        private ApiPaths() {
        }
    }

    public static final class Profiles {
        public static final String NEW_CREATED = "profile-new-created";
        public static final String RECOMMENDATION_SCENARIO = "profile-recommendation-scenario";
        public static final String PERSONALIZED = "profile-personalized";
        public static final String DEFAULT = "profile-default";
        public static final String UNKNOWN = "profile-unknown";

        private Profiles() {
        }
    }
}
