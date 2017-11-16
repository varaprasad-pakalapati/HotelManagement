package extension.handlers;

import java.lang.reflect.Method;

public interface DynamicHandler {
    boolean canHandle(Method var1);
}
