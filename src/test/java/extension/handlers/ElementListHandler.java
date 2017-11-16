package extension.handlers;

import extension.ElementListImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ElementListHandler implements InvocationHandler {
    private List elementList;
    private ElementListImpl extensionsListHandler;

    public ElementListHandler(List element, ElementListImpl extensionsHandler) {
        this.elementList = element;
        this.extensionsListHandler = extensionsHandler;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return this.extensionsListHandler.canHandle(method) ? method.invoke(this.extensionsListHandler, args) : method.invoke(this.elementList, args);
        } catch (InvocationTargetException var5) {
            throw var5.getCause();
        }
    }
}
