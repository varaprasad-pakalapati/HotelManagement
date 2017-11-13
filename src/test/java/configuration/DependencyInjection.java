package configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.openqa.selenium.WebDriver;

public class DependencyInjection extends AbstractModule implements InjectorSource {

    private Injector injector;

    @Override
    protected void configure() {
        bind(WebDriver.class).toProvider(Driver.class).in(ScenarioScoped.class);
    }

    @Override
    public Injector getInjector() {
        if (injector != null)
            return injector;
        injector = Guice.createInjector(CucumberModules.SCENARIO, this);
        return injector;
    }
}
