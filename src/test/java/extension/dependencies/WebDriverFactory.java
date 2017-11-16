package extension.dependencies;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.function.BiConsumer;

@Singleton
public class WebDriverFactory implements DependencyFactory<WebDriver>, Provider<WebDriver> {
    private WebDriver driver;

    public WebDriver get() {
        if (this.driver != null) {
            return this.driver;
        } else {
            this.driver = this.createNewDriver();
            return this.driver;
        }
    }

    private WebDriver createNewDriver() {
        String webdriverProperty = System.getProperty("selenium.webdriver", "firefoxCapabilities");
        String webDriverServer = System.getProperty("selenium.webdriver.remote.server", (String)null);
        if (webDriverServer != null) {
            System.setProperty("webdriver.remote.server", webDriverServer);
        }

        if (webdriverProperty.equalsIgnoreCase("remote")) {
            final DesiredCapabilities capabilities = new DesiredCapabilities();
            System.getProperties().forEach(new BiConsumer<Object, Object>() {
                public void accept(Object o, Object o2) {
                    if (o.toString().matches("selenium.webdriver.remote..*")) {
                        String capabilityName = o.toString().replace("selenium.webdriver.remote.", "");
                        capabilities.setCapability(capabilityName, o2.toString());
                    }

                }
            });
            return new RemoteWebDriver(capabilities);
        } else if (webdriverProperty.equalsIgnoreCase("chrome")) {
            return new ChromeDriver();
        } else if (!webdriverProperty.equalsIgnoreCase("ie") && !webdriverProperty.equalsIgnoreCase("iexplore") && !webdriverProperty.equalsIgnoreCase("internet explorer")) {
            FirefoxProfile firefoxProfile = new FirefoxProfile();
            //firefoxProfile.setEnableNativeEvents(false);
            DesiredCapabilities firefoxCapabilities = DesiredCapabilities.firefox();
            firefoxCapabilities.setCapability("marionette", true);
            firefoxCapabilities.setCapability("firefox_profile", firefoxProfile);
            System.setProperty("webdriver.gecko.driver", "geckodriver");
            return new FirefoxDriver(firefoxCapabilities);
        } else {
            return new InternetExplorerDriver();
        }
    }

    @Override
    public WebDriver invoke(WebDriver request) {
        return this.createNewDriver();
    }
}

