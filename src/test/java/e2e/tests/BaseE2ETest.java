package e2e.tests;

import common.Constants;
import e2e.E2eTestConfig;
import helpers.TestFailureReportingExtension;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ui.tests.AbstractPlaywrightSupport;
import ui.tests.UiAllureScreenshotExtension;

/**
 * Tests that use both WireMock/RestAssured API and Playwright UI in one flow.
 */
@Epic("LOCAL-SURVEY")
@Feature("e2e")
@ExtendWith({SpringExtension.class, TestFailureReportingExtension.class, UiAllureScreenshotExtension.class})
@ContextConfiguration(classes = E2eTestConfig.class)
@Tag(Constants.Tags.E2E)
@Execution(ExecutionMode.CONCURRENT)
public abstract class BaseE2ETest extends AbstractPlaywrightSupport {
}
