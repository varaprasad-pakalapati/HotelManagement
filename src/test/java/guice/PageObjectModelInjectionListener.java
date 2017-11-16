package guice;

import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.spi.InjectionListener;
import extension.PageFactory;

class PageObjectModelInjectionListener implements InjectionListener {
    private Provider<Injector> injector;

    public PageObjectModelInjectionListener(Provider<Injector> injector) {
        this.injector = injector;
    }

    public void afterInjection(Object page) {
        PageFactory pageFactory = (PageFactory)((Injector)this.injector.get()).getInstance(PageFactory.class);
        pageFactory.get(page);
    }
}
