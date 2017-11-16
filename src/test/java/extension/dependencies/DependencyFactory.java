package extension.dependencies;

import javax.xml.ws.Provider;

public interface DependencyFactory<T> extends Provider<T> {
    T get();
}
