package ui.tests;

import common.Constants;
import helpers.TestFailureReportingExtension;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ui.UiTestConfig;

/**
 * UI tests run concurrently (JUnit parallel + {@link ui.driver.PlaywrightFactory} ThreadLocal per thread).
 */
@Epic("LOCAL-SURVEY")
@Feature("ui")
@ExtendWith({SpringExtension.class, TestFailureReportingExtension.class, UiAllureScreenshotExtension.class})
@ContextConfiguration(classes = UiTestConfig.class)
@Tag(Constants.Tags.UI)
@Execution(ExecutionMode.CONCURRENT)
public abstract class BaseUiTest extends AbstractPlaywrightSupport {
}
