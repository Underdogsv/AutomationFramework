package org.example.ui.pom;

/**
 * Locators for the interest survey (mock / fixture markup).
 * Conditional elements are documented in {@link SurveyPage}.
 */
public final class SurveyLocators {
    public static final String SURVEY_ROOT = "[data-testid='survey-root']";
    public static final String GENRE_CHECKBOX = ".genre";
    public static final String NEXT_BUTTON = "#next";
    public static final String SKIP_BUTTON = "#skip";
    public static final String STEP_INDICATOR = "[data-testid='survey-step']";

    /** Shown only when onboarding survey should be displayed (one-time). */
    public static final String SURVEY_OVERLAY = "[data-testid='survey-overlay']";

    private SurveyLocators() {
    }
}
