package extension;

public interface WebElementExtensions {
    boolean isPresent();

    String getValue();

    void set(String var1);

    String getHiddenText();

    void set(String var1, Object... var2);

    void doubleClick();

    void dropOnto(PageElement var1);

    PageElement waitFor(Integer var1);

    PageElement waitFor();

    void waitUntilGone(Integer var1);

    void waitUntilGone();

    PageElement waitUntilHidden(Integer var1);

    PageElement waitUntilHidden();

    PageElement waitUntilVisible(Integer var1);

    PageElement waitUntilVisible();

    PageElement waitUntilStopsMoving(Integer var1);

    PageElement waitUntilStopsMoving();
}
