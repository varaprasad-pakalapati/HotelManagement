package configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;
import cucumber.runtime.java.guice.ScenarioScoped;
import extension.dependencies.DependencyInjector;
import extension.dependencies.InjectionError;
import org.openqa.selenium.WebDriver;

public class DependencyInjection extends AbstractModule implements InjectorSource, DependencyInjector {

    private Injector injector;

    @Override
    protected void configure() {
        bind(WebDriver.class).toProvider(Driver.class).in(ScenarioScoped.class);
    }

    @Provides
    public WebDriver getWebDriver() {
        return this.getInjector().getInstance(WebDriver.class);
    }

    @Override
    public Injector getInjector() {
        if (injector != null)
            return injector;
        injector = Guice.createInjector(CucumberModules.SCENARIO, this);
        return injector;
    }

    @Override
    public <T> T get(Class<T> aClass) throws InjectionError {
        return getInjector().getInstance(aClass);
    }
}
