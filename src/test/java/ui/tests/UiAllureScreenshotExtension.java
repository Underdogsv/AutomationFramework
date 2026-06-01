package ui.tests;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

/**
 * Attaches a Playwright screenshot to Allure when a UI test fails.
 * Call {@link #bindPage(Page)} from {@link BaseUiTest} before the test runs.
 */
public class UiAllureScreenshotExtension implements TestWatcher {

    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    public static void bindPage(Page page) {
        PAGE.set(page);
    }

    public static void clear() {
        PAGE.remove();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Page page = PAGE.get();
        if (page == null) {
            return;
        }
        try {
            byte[] screenshot = page.screenshot();
            Allure.addAttachment("screenshot", "image/png", new java.io.ByteArrayInputStream(screenshot), "png");
        } catch (Exception ignored) {
            // Page may already be closed; do not mask the test failure
        }
    }
}
