package extension;

import extension.handlers.Refreshable;
import extension.handlers.WebElementHandler;
import extension.webdriverConditions.ElementLocationStaticCondition;
import extension.webdriverConditions.ElementPresentCondition;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class PageElementImpl implements PageElement {
    private static final Integer DEFAULT_TIMEOUT = 5000;
    private WebElement webElement;
    private Refreshable parent;

    public PageElementImpl(WebElement webElement) {
        this.webElement = webElement;
    }

    public boolean canHandle(Method methodName) {
        Method[] var2 = this.getClass().getMethods();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Method method = var2[var4];
            if (method.getName().equals(methodName.getName())) {
                return true;
            }
        }

        return false;
    }

    public boolean isPresent() {
        try {
            this.webElement.getTagName();
            return true;
        } catch (NoSuchElementException var2) {
            return false;
        } catch (StaleElementReferenceException var3) {
            return false;
        }
    }

    public String getHiddenText() {
        return (String)((JavascriptExecutor)this.getDriver()).executeScript("return arguments[0].innerText;", new Object[]{this.webElement});
    }

    public String getValue() {
        return this.webElement.getAttribute("value");
    }

    public void set(String text) {
        String tagName = this.webElement.getTagName();
        if (tagName.equalsIgnoreCase("input")) {
            this.webElement.clear();
            this.webElement.sendKeys(new CharSequence[]{text});
        } else if (tagName.equalsIgnoreCase("select")) {
            Select select = new Select(this.webElement);
            select.selectByValue(text);
        } else {
            throw new Error("Cannot set elements value: " + tagName);
        }
    }

    public void set(String format, Object... args) {
        this.set(String.format(format, args));
    }

    public void doubleClick() {
        (new Actions(this.getDriver())).doubleClick(this.webElement).perform();
    }

    public void dropOnto(PageElement target) {
        (new Actions(this.getDriver())).dragAndDrop(this.webElement, target).perform();
    }

    public PageElement waitFor(Integer timeout) {
        this.getWebDriverWait((long)timeout).until(new ElementPresentCondition(this.webElement));
        return this;
    }

    public PageElement waitFor() {
        return this.waitFor(DEFAULT_TIMEOUT);
    }

    public void waitUntilGone(Integer timeout) {
        this.getWebDriverWait((long)timeout).until(ExpectedConditions.not(new ElementPresentCondition(this.webElement)));
    }

    public void waitUntilGone() {
        this.waitUntilGone(DEFAULT_TIMEOUT);
    }

    public PageElement waitUntilHidden(Integer timeout) {
        this.getWebDriverWait((long)timeout).until(ExpectedConditions.not(ExpectedConditions.visibilityOf(this.webElement)));
        return this;
    }

    public PageElement waitUntilHidden() {
        return this.waitUntilHidden(DEFAULT_TIMEOUT);
    }

    public PageElement waitUntilVisible(Integer timeout) {
        this.getWebDriverWait((long)timeout).until(ExpectedConditions.visibilityOf(this.webElement));
        return this;
    }

    public PageElement waitUntilVisible() {
        return this.waitUntilVisible(DEFAULT_TIMEOUT);
    }

    public PageElement waitUntilStopsMoving() {
        return this.waitUntilStopsMoving(DEFAULT_TIMEOUT);
    }

    public PageElement waitUntilStopsMoving(Integer timeout) {
        this.getWebDriverWait((long)timeout.intValue()).until(new ElementLocationStaticCondition(this.webElement));
        return this;
    }

    public WebElement getWrappedElement() {
        return this.webElement;
    }

    public void click() {
        this.webElement.click();
    }

    public void submit() {
        this.webElement.submit();
    }

    public void sendKeys(CharSequence... charSequences) {
        this.webElement.sendKeys(charSequences);
    }

    public void clear() {
        this.webElement.clear();
    }

    public String getTagName() {
        return this.webElement.getTagName();
    }

    public String getAttribute(String s) {
        return this.webElement.getAttribute(s);
    }

    public boolean isSelected() {
        return this.webElement.isSelected();
    }

    public boolean isEnabled() {
        return this.webElement.isEnabled();
    }

    public String getText() {
        return this.webElement.getText();
    }

    public List<WebElement> findElements(By by) {
        return this.webElement.findElements(by);
    }

    public WebElement findElement(By by) {
        return this.webElement.findElement(by);
    }

    public boolean isDisplayed() {
        return this.webElement.isDisplayed();
    }

    public Point getLocation() {
        return this.webElement.getLocation();
    }

    public Dimension getSize() {
        return this.webElement.getSize();
    }

    public Rectangle getRect() {
        return this.webElement.getRect();
    }

    public String getCssValue(String s) {
        return this.webElement.getCssValue(s);
    }

    private WebDriverWait getWebDriverWait(long timeout) {
        return this.getWebDriverWait(timeout, 100L);
    }

    private WebDriverWait getWebDriverWait(long timeout, long interval) {
        return new WebDriverWait(this.getDriver(), (long)Math.round((float)timeout / 1000.0F), interval);
    }

    public void invalidate() {
        InvocationHandler invocationHandler = java.lang.reflect.Proxy.getInvocationHandler(this.webElement);
        if (invocationHandler != null && invocationHandler instanceof Refreshable) {
            ((Refreshable)invocationHandler).invalidate();
        }
    }

    public void refresh() {
        if (this.parent != null) {
            this.parent.refresh();
        }
    }

    public void setParent(Refreshable refreshable) {
        this.parent = refreshable;
    }

    private WebDriver getDriver() {
        if (this.webElement instanceof WrapsDriver) {
            return ((WrapsDriver)this.webElement).getWrappedDriver();
        } else {
            if (this.webElement instanceof java.lang.reflect.Proxy) {
                InvocationHandler invocationHandler = Proxy.getInvocationHandler(this.webElement);
                if (invocationHandler instanceof WebElementHandler) {
                    return ((WebElementHandler)invocationHandler).getDriver();
                }
            }

            return null;
        }
    }

    public <X> X getScreenshotAs(OutputType<X> target) {
        return this.webElement.getScreenshotAs(target);
    }
}

