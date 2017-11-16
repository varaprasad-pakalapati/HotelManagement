package extension;

import extension.handlers.Refreshable;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class PageSection implements SearchContext, Refreshable, WebElementExtensions {
    private static final Integer DEFAULT_WAIT_TIMEOUT = 20000;
    protected PageElement rootElement;
    private Refreshable parent;

    public PageSection() {
    }

    public String getValue() {
        return this.rootElement.getValue();
    }

    public void set(String value) {
        this.rootElement.set(value);
    }

    public String getHiddenText() {
        return this.rootElement.getHiddenText();
    }

    public void set(String format, Object... args) {
        this.rootElement.set(format, args);
    }

    public void doubleClick() {
        this.rootElement.doubleClick();
    }

    public void dropOnto(PageElement pageElement) {
        this.rootElement.dropOnto(pageElement);
    }

    public PageElement waitFor(Integer timeout) {
        return this.rootElement.waitFor(timeout);
    }

    public PageElement waitFor() {
        return this.rootElement.waitFor();
    }

    public PageElement waitUntilHidden(Integer timeout) {
        return this.waitUntilHidden(timeout);
    }

    public PageElement waitUntilHidden() {
        return this.rootElement.waitUntilHidden();
    }

    public PageElement waitUntilVisible(Integer timeout) {
        return this.rootElement.waitUntilVisible(timeout);
    }

    public PageElement waitUntilVisible() {
        return this.rootElement.waitUntilVisible();
    }

    public boolean isPresent() {
        return this.rootElement.isPresent();
    }

    public String getText() {
        return this.rootElement.getText();
    }

    public void waitUntilGone() {
        this.waitUntilGone(DEFAULT_WAIT_TIMEOUT);
    }

    public void waitUntilGone(Integer timeout) {
        this.rootElement.waitUntilGone(timeout);
    }

    public PageElement waitUntilStopsMoving() {
        return this.rootElement.waitUntilStopsMoving();
    }

    public PageElement waitUntilStopsMoving(Integer timeout) {
        return this.rootElement.waitUntilStopsMoving(timeout);
    }

    public List<WebElement> findElements(By by) {
        return this.rootElement.findElements(by);
    }

    public void setParent(Refreshable refreshable) {
        this.parent = refreshable;
    }

    public WebElement findElement(By by) {
        return this.rootElement.findElement(by);
    }

    public void invalidate() {
        this.rootElement.invalidate();
    }

    public void refresh() {
        this.rootElement.refresh();
    }
}

