package extension.dependencies;

import extension.PageFactory;

public class InternalGuiceDependencyInjector extends GuiceDependencyInjector {
    private PageFactory pageFactory;
    private final DependencyInjector dependencyInjector;

    public InternalGuiceDependencyInjector(PageFactory pageFactory, DependencyInjector dependencyInjector, DependencyFactory... factories) {
        super(pageFactory, factories);
        this.modules.add(new FieldInitialiserDependencyModule());
        this.pageFactory = pageFactory;
        this.dependencyInjector = dependencyInjector;
    }

    protected void configure() {
        super.configure();
        this.bind(DependencyInjector.class).toInstance(this.dependencyInjector);
    }
}
