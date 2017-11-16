package extension;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import extension.annotations.Frame;
import extension.configuration.Constants;
import extension.dependencies.*;
import extension.exceptions.PageFactoryError;
import extension.fieldinitialisers.FieldAssessor;
import extension.fieldinitialisers.FieldInitialiser;
import extension.fromselenium.Annotations;
import extension.helpers.ClassHelper;
import extension.handlers.Refreshable;
import extension.helpers.FrameWrapper;
import extension.helpers.ImplementationFinder;
import extension.helpers.ReflectionHelper;
import extension.orchestration.WebDriverFrameSwitchingOrchestrator;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Singleton
public class PageFactory implements WebDriverEventListener, DependencyFactory<PageFactory> {
    private static final Logger LOG = Logger.getLogger(PageFactory.class.getName());
    @Inject
    public DependencyInjector dependencyInjector;
    @Inject
    private Set<FieldInitialiser> fieldInitialisers;
    private EventFiringWebDriver eventFiringWebDriver;
    @Inject
    private WebDriverFrameSwitchingOrchestrator webDriverOrchestrator;

    public PageFactory() {
        this(new WebDriverFactory());
    }

    public PageFactory(DependencyFactory... factories) {
        this((DependencyInjector) null, factories);
    }

    public PageFactory(DependencyInjector dependencyInjector, DependencyFactory... factories) {
        LOG.fine("Creating PageFactory");
        DependencyInjector externalDependencyInjector = dependencyInjector;
        if (dependencyInjector == null) {
            externalDependencyInjector = new GuiceDependencyInjector(this, factories);
        }

        GuiceDependencyInjector injector = new InternalGuiceDependencyInjector(this, (DependencyInjector) externalDependencyInjector, new DependencyFactory[0]);
        injector.injectMembers(this);
    }

    public WebDriver getDriver() {
        this.eventFiringWebDriver = new EventFiringWebDriver((WebDriver) this.dependencyInjector.get(WebDriver.class));
        this.eventFiringWebDriver.register(this);
        return this.eventFiringWebDriver;
    }

    public <T> T get(Class<T> pageClass) throws PageFactoryError {
        return this.get(pageClass, this.getDriver());
    }

    public <T> T get(T page) {
        return this.initializeContainer(page, this.getDriver());
    }

    public <T> T get(Class<T> pageClass, SearchContext searchContext) throws PageFactoryError {
        return this.get(pageClass, searchContext, this.getFrame(pageClass, pageClass.getName(), (FrameWrapper) null));
    }

    public <T> T get(Class<T> pageClass, SearchContext searchContext, FrameWrapper frame) throws PageFactoryError {
        T page = this.findImplementationBasedOnPageFilter(pageClass);
        return this.initializeContainer(page, searchContext, frame);
    }

    private <T> T findImplementationBasedOnPageFilter(Class<T> pageClass) {
        ImplementationFinder<T> implementationFinder = new ImplementationFinder(pageClass, this.dependencyInjector);
        return implementationFinder.find();
    }

    protected <T> T initializeContainer(T page, SearchContext searchContext) {
        return this.initializeContainer(page, searchContext, this.getFrame(page.getClass(), page.getClass().getName(), (FrameWrapper) null));
    }

    private FrameWrapper getFrame(AnnotatedElement element, String name, FrameWrapper parentFrame) {
        Frame annotation = (Frame) element.getAnnotation(Frame.class);
        if (annotation == null) {
            return parentFrame;
        } else {
            Annotations annotations = new Annotations(element, name);
            By frameIdentifier = annotations.buildBy();
            FrameWrapper frameWrapper = new FrameWrapper(this.getDriver(), frameIdentifier);
            frameWrapper.setParent(parentFrame);
            return frameWrapper;
        }
    }

    public <T> T initializeContainer(T page, SearchContext searchContext, FrameWrapper frameWrapper) {
        this.setRootElement(page, searchContext);
        Iterator var4 = ClassHelper.getFieldsFromClass(page.getClass()).iterator();

        while (true) {
            Field field;
            do {
                if (!var4.hasNext()) {
                    return page;
                }

                field = (Field) var4.next();
            } while (field.getName().equals(Constants.ROOT_ELEMENT_FIELD_NAME));

            frameWrapper = this.getFrame(field, field.getName(), frameWrapper);
            Iterator var6 = this.fieldInitialisers.iterator();

            while (var6.hasNext()) {
                FieldInitialiser fieldInitialiser = (FieldInitialiser) var6.next();
                if (fieldInitialiser.initialiseField(field, page, searchContext, frameWrapper).booleanValue()) {
                    break;
                }
            }
        }
    }

    private <T> void setRootElement(T pageObject, SearchContext searchContext) {
        if (searchContext instanceof PageElement) {
            try {
                Field rootElement = this.findField(pageObject.getClass(), Constants.ROOT_ELEMENT_FIELD_NAME);
                rootElement.setAccessible(true);
                rootElement.set(pageObject, searchContext);
            } catch (NoSuchFieldException var4) {
                ;
            } catch (IllegalAccessException var5) {
                throw new PageFactoryError(var5.getCause());
            }

        }
    }

    private Field findField(Class<?> klass, String name) throws NoSuchFieldException {
        while (klass != null) {
            Field[] var3 = klass.getDeclaredFields();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                if (field.getName().equalsIgnoreCase(name)) {
                    return field;
                }
            }

            klass = klass.getSuperclass();
        }

        throw new NoSuchFieldException("Could not find field with name " + name);
    }

    public void beforeAlertAccept(WebDriver webDriver) {
    }

    public void afterAlertAccept(WebDriver webDriver) {
    }

    public void afterAlertDismiss(WebDriver webDriver) {
    }

    public void beforeAlertDismiss(WebDriver webDriver) {
    }

    public void beforeNavigateTo(String s, WebDriver webDriver) {
    }

    public void afterNavigateTo(String s, WebDriver webDriver) {
        this.webDriverOrchestrator.useFrame((FrameWrapper) null);
    }

    public void beforeNavigateBack(WebDriver webDriver) {
    }

    public void afterNavigateBack(WebDriver webDriver) {
    }

    public void beforeNavigateForward(WebDriver webDriver) {
    }

    public void afterNavigateForward(WebDriver webDriver) {
    }

    public void beforeNavigateRefresh(WebDriver webDriver) {
    }

    public void afterNavigateRefresh(WebDriver webDriver) {
    }

    public void beforeFindBy(By by, WebElement webElement, WebDriver webDriver) {
    }

    public void afterFindBy(By by, WebElement webElement, WebDriver webDriver) {
    }

    public void beforeClickOn(WebElement webElement, WebDriver webDriver) {
    }

    public void afterClickOn(WebElement webElement, WebDriver webDriver) {
    }

    public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
    }

    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
    }

    public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver) {
    }

    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver) {
    }

    public void beforeScript(String s, WebDriver webDriver) {
    }

    public void afterScript(String s, WebDriver webDriver) {
    }

    public void onException(Throwable throwable, WebDriver webDriver) {
    }

    @Provides
    public PageFactory get() {
        LOG.fine("Got request for PageFactory");
        return this;
    }

    public void invalidate(Object object) {
        if (object instanceof Refreshable) {
            ((Refreshable) object).invalidate();
        }

        if (object instanceof Proxy) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(object);
            if (invocationHandler instanceof Refreshable) {
                ((Refreshable) invocationHandler).invalidate();
            }
        } else {
            List<Field> fields = ReflectionHelper.getAllFields(object);
            Iterator var3 = fields.iterator();

            while (var3.hasNext()) {
                Field field = (Field) var3.next();
                if (FieldAssessor.isSeleniumPomField(field)) {
                    Object fieldValue = ReflectionHelper.getFieldValue(object, field.getName());
                    this.invalidate(fieldValue);
                }
            }
        }

    }

    @Override
    public PageFactory invoke(PageFactory request) {
        return get();
    }
}
