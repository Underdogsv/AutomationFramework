package ui.driver;

import com.microsoft.playwright.*;
import org.springframework.stereotype.Component;
import ui.config.UiProperties;

import java.util.List;

/**
 * One Playwright browser stack per JUnit worker thread — safe for parallel UI tests.
 */
@Component
public class PlaywrightFactory {

    private final UiProperties props;

    private final ThreadLocal<Playwright> playwrightTl = new ThreadLocal<>();
    private final ThreadLocal<Browser> browserTl = new ThreadLocal<>();
    private final ThreadLocal<BrowserContext> contextTl = new ThreadLocal<>();
    private final ThreadLocal<Page> pageTl = new ThreadLocal<>();

    public PlaywrightFactory(UiProperties props) {
        this.props = props;
    }

    public Page getPage() {
        Page page = pageTl.get();
        if (page == null) {
            page = createPage();
            pageTl.set(page);
        }
        return page;
    }

    private Page createPage() {
        Playwright playwright = Playwright.create();
        playwrightTl.set(playwright);

        BrowserType browserType = switch (props.getBrowser().toLowerCase()) {
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            default -> playwright.chromium();
        };

        Browser browser = browserType.launch(new BrowserType.LaunchOptions()
                .setHeadless(props.isHeadless())
                .setSlowMo(props.getSlowMo())
                .setArgs(List.of(
                        "--disable-gpu",
                        "--disable-dev-shm-usage")));
        browserTl.set(browser);

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(props.getViewportWidth(), props.getViewportHeight()));
        contextTl.set(context);

        Page page = context.newPage();
        page.setDefaultTimeout(30_000);
        return page;
    }

    public void cleanup() {
        Page page = pageTl.get();
        if (page != null) {
            page.close();
        }
        BrowserContext ctx = contextTl.get();
        if (ctx != null) {
            ctx.close();
        }
        Browser br = browserTl.get();
        if (br != null) {
            br.close();
        }
        Playwright pw = playwrightTl.get();
        if (pw != null) {
            pw.close();
        }

        pageTl.remove();
        contextTl.remove();
        browserTl.remove();
        playwrightTl.remove();
    }
}
