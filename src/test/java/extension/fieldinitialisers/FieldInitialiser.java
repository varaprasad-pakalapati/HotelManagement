package extension.fieldinitialisers;

import extension.helpers.FrameWrapper;
import org.openqa.selenium.SearchContext;

import java.lang.reflect.Field;

public interface FieldInitialiser {
    Boolean initialiseField(Field var1, Object var2, SearchContext var3, FrameWrapper var4);
}
