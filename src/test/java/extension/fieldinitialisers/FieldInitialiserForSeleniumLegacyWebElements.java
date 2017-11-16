package extension.fieldinitialisers;

import com.google.inject.Inject;
import extension.dependencies.DependencyInjector;
import extension.handlers.WebElementHandler;
import extension.helpers.FrameWrapper;
import extension.orchestration.WebDriverFrameSwitchingOrchestrator;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.Annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

public class FieldInitialiserForSeleniumLegacyWebElements implements FieldInitialiser {

    @Inject
    private DependencyInjector dependencyInjector;
    @Inject
    private WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator;

    public Boolean initialiseField(Field field, Object page, SearchContext searchContext, FrameWrapper frame) {
        if (!FieldAssessor.isValidWebElement(field)) {
            return false;
        } else {
            Annotations annotations = new Annotations(field);

            try {
                WebElementHandler elementHandler = new WebElementHandler(this.dependencyInjector, searchContext, annotations.buildBy(), frame, this.webDriverFrameSwitchingOrchestrator);
                WebElement proxyElement = (WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(), new Class[]{WebElement.class, SearchContext.class, WrapsElement.class}, elementHandler);
                field.setAccessible(true);
                field.set(page, proxyElement);
            } catch (IllegalAccessException var8) {
                var8.printStackTrace();
            }

            return true;
        }
    }
}
