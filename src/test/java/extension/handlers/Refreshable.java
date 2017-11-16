package extension.handlers;

public interface Refreshable {

    void invalidate();

    void refresh();

    void setParent(Refreshable var1);
}
