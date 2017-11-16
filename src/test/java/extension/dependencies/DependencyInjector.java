package extension.dependencies;

public interface DependencyInjector {

    <T> T get(Class<T> value) throws InjectionError;
}
