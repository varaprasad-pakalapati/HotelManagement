package extension.dependencies;

public class InjectionError extends Error {
    public InjectionError(Throwable cause) {
        super(cause);
    }

    public InjectionError(String message) {
        super(message);
    }
}
