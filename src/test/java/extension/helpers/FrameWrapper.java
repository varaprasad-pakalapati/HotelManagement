package extension.helpers;

import extension.annotations.Frame;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrameWrapper {
    private static final Logger LOG = Logger.getLogger(Frame.class.getName());
    private WebDriver driver;
    private FrameWrapper parent;
    public By frameBy;

    public FrameWrapper(WebDriver driver, By frameBy) {
        this.driver = driver;
        this.frameBy = frameBy;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof FrameWrapper)) {
            return false;
        } else {
            FrameWrapper that = (FrameWrapper)o;
            return this.hashCode() == that.hashCode();
        }
    }

    public int hashCode() {
        if (this.frameBy == null) {
            return 0;
        } else {
            int result = this.frameBy.toString().hashCode();
            if (this.parent != null) {
                result += this.parent.hashCode();
            }

            result = 31 * result;
            return result;
        }
    }

    public void use() {
        if (this.parent != null) {
            this.parent.use();
        }

        LOG.log(Level.FINE, "Switching to frame {0}", this);
        WebElement frameElement = this.driver.findElement(this.frameBy);
        this.driver.switchTo().frame(frameElement);
    }

    public FrameWrapper setParent(FrameWrapper parent) {
        this.parent = parent;
        return this;
    }

    public String toString() {
        ArrayList<String> frameWrappers = new ArrayList();

        for(FrameWrapper frame = this; frame != null; frame = frame.parent) {
            frameWrappers.add(frame.frameBy.toString());
        }

        return ArrayHelper.join(frameWrappers, " inside -> ");
    }
}

