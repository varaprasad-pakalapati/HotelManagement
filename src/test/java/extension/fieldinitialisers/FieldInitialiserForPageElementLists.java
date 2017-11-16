package extension.fieldinitialisers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import extension.PageFactory;
import extension.dependencies.DependencyInjector;
import extension.exceptions.PageFactoryError;
import extension.handlers.PageElementListHandler;
import extension.helpers.FrameWrapper;
import extension.orchestration.WebDriverFrameSwitchingOrchestrator;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.Annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;

public class FieldInitialiserForPageElementLists implements FieldInitialiser {
    @Inject
    private DependencyInjector dependencyInjector;
    @Inject
    private WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator;
    @Inject
    private Provider<PageFactory> pageFactory;

    public FieldInitialiserForPageElementLists() {
    }

    public Boolean initialiseField(Field field, Object page, SearchContext searchContext, FrameWrapper frame) {
        if (!FieldAssessor.isValidPageElementList(field)) {
            return false;
        } else {
            Annotations annotations = new Annotations(field);
            PageElementListHandler elementListHandler = new PageElementListHandler(this.dependencyInjector, searchContext, annotations.buildBy(), frame, this.webDriverFrameSwitchingOrchestrator);
            List webElementListProxy = (List) Proxy.newProxyInstance(WebElement.class.getClassLoader(), new Class[]{List.class}, elementListHandler);

            try {
                field.setAccessible(true);
                field.set(page, webElementListProxy);
            } catch (IllegalAccessException var9) {
                throw new PageFactoryError(var9.getCause());
            }

            return true;
        }
    }
}
