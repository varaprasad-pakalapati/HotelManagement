package configuration;

import com.google.inject.Inject;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.WebDriver;
import steps.HotelManagementSteps;

public class Hooks {

    @Inject
    private WebDriver driver;

    @Before
    public void before() {
        HotelManagementSteps.entryList.clear();
    }

    @After
    public void after() {
        driver.quit();
    }

}
