package extension.orchestration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import extension.dependencies.DependencyInjector;
import extension.helpers.FrameWrapper;
import org.openqa.selenium.WebDriver;

import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class WebDriverFrameSwitchingOrchestrator {
    private static final Logger LOG = Logger.getLogger(WebDriverFrameSwitchingOrchestrator.class.getName());
    private FrameWrapper frame;
    @Inject
    DependencyInjector dependencyInjector;

    public WebDriverFrameSwitchingOrchestrator() {
    }

    public void useFrame(FrameWrapper frame) {
        LOG.log(Level.FINE, "Called use frame with {0}", frame);
        if (frame == null) {
            this.useDefault();
        } else if (this.frame == null || !this.frame.equals(frame)) {
            this.useDefault(true);
            this.frame = frame;
            frame.use();
        }
    }

    private void useDefault() {
        this.useDefault(false);
    }

    private void useDefault(boolean force) {
        if (force || this.frame != null) {
            this.frame = null;
            LOG.log(Level.FINE, "Switching to default content");
            this.dependencyInjector.get(WebDriver.class).switchTo().defaultContent();
        }
    }
}
