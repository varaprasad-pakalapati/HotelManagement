package extension;

import extension.handlers.DynamicHandler;
import extension.handlers.Refreshable;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;

public interface PageElement extends WebElement, WebElementExtensions, WrapsElement, SearchContext, DynamicHandler, Refreshable {
}
