package extension.helpers;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClassHelper {
    public ClassHelper() {
    }

    public static Iterable<Field> getFieldsFromClass(Class<?> startClass) {
        List<Field> currentClassFields = new ArrayList();
        Class<?> parentClass = startClass.getSuperclass();
        if (parentClass != null) {
            List<Field> parentClassFields = (List)getFieldsFromClass(parentClass);
            currentClassFields.addAll(parentClassFields);
        }

        currentClassFields.addAll(Lists.newArrayList(startClass.getDeclaredFields()));
        return currentClassFields;
    }
}
