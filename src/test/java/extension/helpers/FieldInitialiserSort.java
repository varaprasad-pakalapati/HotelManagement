package extension.helpers;

import extension.fieldinitialisers.FieldInitialiser;

import java.util.Comparator;

public class FieldInitialiserSort implements Comparator<Class<? extends FieldInitialiser>> {
    public FieldInitialiserSort() {
    }

    public int compare(Class<? extends FieldInitialiser> aClass, Class<? extends FieldInitialiser> t1) {
        return aClass.getSimpleName().compareTo(t1.getSimpleName());
    }
}
