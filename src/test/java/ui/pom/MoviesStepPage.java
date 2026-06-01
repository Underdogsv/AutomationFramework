package ui.pom;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import common.Constants;

/** Step 2: pick movies after genres. */
public class MoviesStepPage {
    private final Page page;
    private final Locator root;
    private final Locator visibleMovies;
    private final Locator finishButton;

    public MoviesStepPage(Page page) {
        this.page = page;
        this.root = page.locator("[data-step=movies]");
        this.visibleMovies = page.locator(".movie-row:not([hidden]) .movie");
        this.finishButton = page.locator("#finish");
    }

    public boolean isVisible() {
        return root.isVisible();
    }

    public void selectMovies(int count) {
        for (int i = 0; i < count; i++) {
            visibleMovies.nth(i).check();
        }
    }

    public int visibleMovieCount() {
        return visibleMovies.count();
    }

    public int selectedMovieCount() {
        return (int) page.locator(".movie:checked").count();
    }

    public boolean isFinishEnabled() {
        return finishButton.isEnabled();
    }

    public boolean isFinishVisible() {
        return finishButton.isVisible();
    }

    public void clickFinish() {
        finishButton.click();
    }

    public int expectedMovieCount() {
        return Constants.Survey.EXPECTED_MOVIES_ON_STEP_TWO;
    }
}
