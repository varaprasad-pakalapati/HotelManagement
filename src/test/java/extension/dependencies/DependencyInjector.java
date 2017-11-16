package extension.dependencies;

public interface DependencyInjector {

    <T> T get(Class<T> var1) throws InjectionError;
}
