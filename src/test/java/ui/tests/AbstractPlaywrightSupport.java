package ui.tests;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import ui.driver.PlaywrightFactory;
import ui.pom.MoviesStepPage;
import ui.pom.SurveyStepPage;
import utils.FixtureUtils;

/**
 * Shared Playwright lifecycle and page helpers for UI and E2E tests.
 */
public abstract class AbstractPlaywrightSupport {

    @Autowired
    protected PlaywrightFactory pw;

    protected Page page() {
        return pw.getPage();
    }

    @BeforeEach
    void bindPageForAllureScreenshot() {
        UiAllureScreenshotExtension.bindPage(page());
    }

    @AfterEach
    void teardownBrowser() {
        UiAllureScreenshotExtension.clear();
        pw.cleanup();
    }

    protected SurveyStepPage openSurvey() {
        page().navigate(FixtureUtils.surveyFixtureUrl());
        return new SurveyStepPage(page());
    }

    protected MoviesStepPage moviesStep() {
        return new MoviesStepPage(page());
    }
}
