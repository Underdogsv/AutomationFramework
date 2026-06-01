package ui.tests;

import api.tests.BaseApiTest;
import com.microsoft.playwright.Page;
import common.Constants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import ui.driver.PlaywrightFactory;
import ui.pom.MoviesStepPage;
import ui.pom.SurveyStepPage;
import utils.FixtureUtils;

/**
 * UI tests run concurrently (JUnit parallel + {@link PlaywrightFactory} ThreadLocal per thread).
 */
@Epic("LOCAL-SURVEY")
@Feature("ui")
@ExtendWith(UiAllureScreenshotExtension.class)
@Tag(Constants.Tags.UI)
@Execution(ExecutionMode.CONCURRENT)
public abstract class BaseUiTest extends BaseApiTest {

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
