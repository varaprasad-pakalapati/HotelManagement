package configuration;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.google.inject.Provider;
import java.util.concurrent.TimeUnit;

public class Driver implements Provider<WebDriver> {

    private WebDriver driver;

    @Override
    public WebDriver get() {
        initializeDriver("chrome");
        return driver;
    }

    private void initializeDriver(String browser) {
        switch (browser.toUpperCase()) {
            case "FIREFOX":
                System.setProperty("webdriver.gecko.driver", "browserdrivers/geckodriver.exe");
                driver = new FirefoxDriver();
                break;
            case "CHROME":
                System.setProperty("webdriver.chrome.driver", "browserdrivers/chromedriver.exe");
                driver = new ChromeDriver(getChromeOptions());
                break;
            case "IE":
                System.setProperty("webdriver.ie.driver", "browserdrivers/IEDriverServer.exe");
                driver = new InternetExplorerDriver();
                break;
            default:
                System.setProperty("webdriver.gecko.driver", "browserdrivers/geckodriver.exe");
                driver = new FirefoxDriver();
                break;
        }

        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");

        return chromeOptions;
    }
}
