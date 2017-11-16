package extension.fieldinitialisers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import extension.ElementListImpl;
import extension.PageElement;
import extension.PageFactory;
import extension.dependencies.DependencyInjector;
import extension.exceptions.PageFactoryError;
import extension.handlers.ElementListHandler;
import extension.handlers.WebElementListHandler;
import extension.helpers.FrameWrapper;
import extension.orchestration.WebDriverFrameSwitchingOrchestrator;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.Annotations;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

public class FieldInitialiserForSeleniumLegacyWebElementLists implements FieldInitialiser {
    @Inject
    private DependencyInjector dependencyInjector;
    @Inject
    private WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator;
    @Inject
    private Provider<PageFactory> pageFactory;

    public FieldInitialiserForSeleniumLegacyWebElementLists() {
    }

    public Boolean initialiseField(Field field, Object page, SearchContext searchContext, FrameWrapper frame) {
        if (!FieldAssessor.isValidWebElementList(field)) {
            return false;
        } else {
            Annotations annotations = new Annotations(field);
            WebElementListHandler elementListHandler = new WebElementListHandler(this.dependencyInjector, searchContext, annotations.buildBy(), frame, this.webDriverFrameSwitchingOrchestrator);
            List webElementListProxy = (List) Proxy.newProxyInstance(WebElement.class.getClassLoader(), new Class[]{List.class}, elementListHandler);
            ElementListImpl webElementListExtensions = new ElementListImpl(searchContext, webElementListProxy);
            InvocationHandler pageElementHandler = new ElementListHandler(webElementListProxy, webElementListExtensions);
            List pageElementListProxy = (List)Proxy.newProxyInstance(PageElement.class.getClassLoader(), new Class[]{List.class}, pageElementHandler);

            try {
                field.setAccessible(true);
                field.set(page, pageElementListProxy);
            } catch (IllegalAccessException var12) {
                throw new PageFactoryError(var12.getCause());
            }

            return true;
        }
    }
}
