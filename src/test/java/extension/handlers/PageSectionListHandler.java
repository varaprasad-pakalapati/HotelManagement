package extension.handlers;

import com.google.inject.Provider;
import extension.PageElementImpl;
import extension.PageFactory;
import extension.PageSection;
import extension.configuration.Constants;
import extension.dependencies.DependencyInjector;
import extension.helpers.FrameWrapper;
import extension.helpers.ReflectionHelper;
import extension.orchestration.WebDriverFrameSwitchingOrchestrator;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PageSectionListHandler implements InvocationHandler, Refreshable {
    private DependencyInjector driver;
    private SearchContext searchContext;
    private By by;
    private Type pageSectionType;
    private Provider<PageFactory> pageFactory;
    private FrameWrapper frame;
    private WebDriverFrameSwitchingOrchestrator webDriverOrchestrator;
    private ArrayList<Object> pageSections;
    private Refreshable parent;

    public PageSectionListHandler(DependencyInjector driver, SearchContext searchContext, By by, Type pageSectionType, Provider<PageFactory> pageFactory, FrameWrapper frame, WebDriverFrameSwitchingOrchestrator webDriverOrchestrator) {
        this.driver = driver;
        this.searchContext = searchContext;
        this.by = by;
        this.pageSectionType = pageSectionType;
        this.pageFactory = pageFactory;
        this.frame = frame;
        this.webDriverOrchestrator = webDriverOrchestrator;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.pageSections == null) {
            List<WebElement> elements = this.searchContext.findElements(this.by);
            this.pageSections = new ArrayList();
            Iterator var5 = elements.iterator();

            while(var5.hasNext()) {
                WebElement element = (WebElement)var5.next();
                Object pageSection = this.getPageSection(element);
                this.pageSections.add(pageSection);
            }
        }

        return method.invoke(this.pageSections, args);
    }

    private Object getPageSection(WebElement element) {
        PageElementImpl pageElement = this.getPageElement(element);
        Class<?> pageSectionClass = (Class)this.pageSectionType;
        return this.pageFactory.get().get(pageSectionClass, pageElement, this.frame);
    }

    private PageElementImpl getPageElement(WebElement element) {
        WebElement elementProxy = this.getWebElement(element);
        PageElementImpl pageElement = new PageElementImpl(elementProxy);
        pageElement.setParent(this);
        return pageElement;
    }

    private WebElement getWebElement(WebElement element) {
        WebElementHandler webElementHandler = new WebElementHandler(this.driver, this.searchContext, By.id(Constants.DUMMY_PAGE_LOCATOR_FOR_LISTS), this.frame, this.webDriverOrchestrator, element);
        webElementHandler.setParent(this);
        return (WebElement) Proxy.newProxyInstance(webElementHandler.getClass().getClassLoader(), new Class[]{WebElement.class}, webElementHandler);
    }

    public void invalidate() {
        if (this.pageSections != null) {

            for (Object pageSection : this.pageSections) {
                this.pageFactory.get().invalidate(pageSection);
            }

        }
    }

    public void refresh() {
        if (this.parent != null) {
            this.parent.refresh();
        }

        if (this.pageSections != null) {
            List<WebElement> elements = this.searchContext.findElements(this.by);
            Field rootElementField = ReflectionHelper.getField(PageSection.class, Constants.ROOT_ELEMENT_FIELD_NAME);
            Field webElementField = ReflectionHelper.getField(PageElementImpl.class, Constants.PAGE_ELEMENT_CONTAINER_FIELD_NAME);
            webElementField.setAccessible(true);
            Field webElementHandlersWebElementField = ReflectionHelper.getField(WebElementHandler.class, Constants.PAGE_ELEMENT_CONTAINER_FIELD_NAME);
            webElementHandlersWebElementField.setAccessible(true);

            assert rootElementField != null;

            rootElementField.setAccessible(true);

            for(int i = 0; i < elements.size(); ++i) {
                WebElement e = this.getWebElement(elements.get(i));
                Object s = this.pageSections.get(i);
                if (s == null) {
                    this.pageSections.set(i, this.getPageSection(e));
                } else {
                    try {
                        Object rootElement = rootElementField.get(s);
                        Object webElementProxy = webElementField.get(rootElement);
                        if (webElementProxy instanceof Proxy) {
                            InvocationHandler invocationHandler = Proxy.getInvocationHandler(webElementProxy);
                            if (invocationHandler instanceof WebElementHandler) {
                                webElementHandlersWebElementField.set(invocationHandler, e);
                            }
                        }
                    } catch (IllegalAccessException var11) {
                        var11.printStackTrace();
                    } catch (IllegalArgumentException var12) {
                        var12.printStackTrace();
                    }
                }
            }

        }
    }

    public void setParent(Refreshable refreshable) {
        this.parent = refreshable;
    }
}

