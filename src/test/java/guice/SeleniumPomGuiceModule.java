package guice;

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import extension.PageFactory;
import extension.dependencies.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SeleniumPomGuiceModule extends AbstractModule implements DependencyInjector {

    private Injector injector;
    private HashMap<Class, DependencyFactory> factories = new HashMap();
    private PageFactory pageFactory;
    private ArrayList<Module> modules = new ArrayList();

    public SeleniumPomGuiceModule(Module... modules) {
        this.modules.add(this);
        Collections.addAll(this.modules, modules);
    }

    @Override
    protected void configure() {
        this.bindListener(Matchers.any(), new PageObjectModelTypeListener());
    }

    @Provides
    @Singleton
    PageFactory providePageFactory() {
        if (this.pageFactory != null) {
            return this.pageFactory;
        } else {
            this.pageFactory = new PageFactory(this, new DependencyFactory[0]);
            return this.pageFactory;
        }
    }

    public Injector getInjector() {
        if (this.injector == null) {
            this.injector = Guice.createInjector(Stage.PRODUCTION, this.modules);
        }

        return this.injector;
    }

    @Override
    public <T> T get(Class<T> aClass) throws InjectionError {
        return this.getInjector().getInstance(aClass);
    }

    public void injectMembers(Object object) {
        this.getInjector().injectMembers(object);
    }
}
