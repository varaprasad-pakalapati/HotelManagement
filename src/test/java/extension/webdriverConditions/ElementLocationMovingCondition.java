package extension.webdriverConditions;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;

public class ElementLocationMovingCondition implements ExpectedCondition<WebElement> {
    private Point location;
    private WebElement element;

    public ElementLocationMovingCondition(WebElement element) {
        this.element = element;
        this.location = element.getLocation();
    }

    @Nullable
    public WebElement apply(@Nullable WebDriver webDriver) {
        Point newLocation = this.element.getLocation();
        if (this.location.equals(newLocation)) {
            this.location = newLocation;
            return null;
        } else {
            return this.element;
        }
    }
}
