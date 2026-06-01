package ui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UiProperties {
    @Value("${ui.browser:chromium}")
    private String browser;

    @Value("${ui.headless:true}")
    private boolean headless;

    @Value("${ui.slowMo:0}")
    private int slowMo;

    @Value("${ui.viewportWidth:1280}")
    private int viewportWidth;

    @Value("${ui.viewportHeight:720}")
    private int viewportHeight;

    public String getBrowser() {
        return browser;
    }

    public boolean isHeadless() {
        String override = System.getProperty("ui.headless");
        if (override != null) {
            return !"false".equalsIgnoreCase(override);
        }
        return headless;
    }

    public int getSlowMo() {
        return slowMo;
    }

    public int getViewportWidth() {
        return viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }
}
