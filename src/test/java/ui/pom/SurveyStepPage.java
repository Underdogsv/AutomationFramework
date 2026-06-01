package ui.pom;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import common.Constants;

/** Step 1: genre selection, Next, Skip. */
public class SurveyStepPage {
    private final Locator root;
    private final Locator skippedStep;
    private final Locator nextButton;
    private final Locator skipButton;
    private final Locator genreCheckboxes;

    public SurveyStepPage(Page page) {
        this.root = page.locator("[data-step=genres]");
        this.skippedStep = page.locator("[data-step=skipped]");
        this.nextButton = page.locator("#next");
        this.skipButton = page.locator("#skip");
        this.genreCheckboxes = page.locator(".genre");
    }

    public boolean isVisible() {
        return root.isVisible();
    }

    public boolean isSkippedScreenVisible() {
        return skippedStep.isVisible();
    }

    public void selectGenres(int count) {
        for (int i = 0; i < count; i++) {
            genreCheckboxes.nth(i).check();
        }
    }

    /**
     * Picks specific genre checkboxes by position (0 = first on screen).
     * Unlike {@link #selectGenres(int)}, which always checks the first N boxes,
     * this is used when a test needs a particular genre set — e.g. AC-3 filtering:
     * genres 1+2+3 vs 1+2+4 must show different movie lists (see movie 107, genre 4 only).
     */
    public void selectGenreByIndexes(int... indexes) {
        for (int index : indexes) {
            genreCheckboxes.nth(index).check();
        }
    }

    public void uncheckGenre(int index) {
        genreCheckboxes.nth(index).uncheck();
    }

    public int selectedGenreCount() {
        return (int) genreCheckboxes.evaluateAll(
                "elements => elements.filter(e => e.checked).length");
    }

    public boolean isNextEnabled() {
        return nextButton.isEnabled();
    }

    public boolean isSkipVisible() {
        return skipButton.isVisible();
    }

    public void clickNext() {
        nextButton.click();
    }

    public void clickSkip() {
        skipButton.click();
    }

    public int minGenresRequired() {
        return Constants.Survey.MIN_GENRES_FOR_NEXT;
    }
}
