package extension.helpers;

import java.util.Iterator;

public class ArrayHelper {
    public ArrayHelper() {
    }

    public static <T> String join(Iterable<T> aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        boolean first = true;
        Iterator var4 = aArr.iterator();

        while(var4.hasNext()) {
            Object o = var4.next();
            if (!first) {
                sbStr.append(sSep);
            }

            first = false;
            sbStr.append(o.toString());
        }

        return sbStr.toString();
    }
}
