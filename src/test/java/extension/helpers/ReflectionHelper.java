package extension.helpers;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectionHelper {
    public ReflectionHelper() {
    }

    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getField(object, fieldName);
        if (field == null) {
            return null;
        } else {
            field.setAccessible(true);

            try {
                return field.get(object);
            } catch (IllegalAccessException var4) {
                return null;
            }
        }
    }

    public static List<Field> getAllFields(Object object) {
        return getAllClassFields(object.getClass());
    }

    public static List<Field> getAllClassFields(Class klass) {
        ArrayList fields;
        for(fields = new ArrayList(); klass != null; klass = klass.getSuperclass()) {
            Collections.addAll(fields, klass.getDeclaredFields());
        }

        return fields;
    }

    public static Field getField(Object object, String fieldName) {
        return getField(object.getClass(), fieldName);
    }

    public static Field getField(Class klass, String fieldName) {
        while(klass != null) {
            try {
                Field field = klass.getDeclaredField(fieldName);
                return field;
            } catch (NoSuchFieldException var3) {
                klass = klass.getSuperclass();
            }
        }

        return null;
    }

    public static boolean isWrapperType(Class<?> clazz) {
        return getWrapperTypes().contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }
}

