package extension.webdriverConditions;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementPresentCondition implements ExpectedCondition<WebElement> {
    private WebElement element;

    public ElementPresentCondition(WebElement element) {
        this.element = element;
    }

    @Nullable
    public WebElement apply(@Nullable WebDriver webDriver) {
        try {
            this.element.getTagName();
            return this.element;
        } catch (StaleElementReferenceException var3) {
            return null;
        }
    }
}