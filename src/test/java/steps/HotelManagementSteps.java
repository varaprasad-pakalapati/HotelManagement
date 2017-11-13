package steps;

import com.google.inject.Inject;
import com.google.inject.Provider;
import configuration.Driver;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import pageobjects.HomePage;
import world.Entry;

import java.util.ArrayList;
import java.util.List;


public class HotelManagementSteps {

    private static HomePage homePage;

    public static List<Entry> entryList = new ArrayList<>();

    @Inject
    private WebDriver driver;

    @Given("^user have hotel management application$")
    public void iHaveHotelManagementApplication() {
        driver.get("http://localhost:3003/");
        homePage = new HomePage(driver);
    }

    @And("^user login with (.*) and (.*)$")
    public void iLoginWithAdminAndPassword(String username, String password) {
        homePage.login(username, password);
    }

    @When("^create an entry$")
    public void createAnEntry(List<Entry> entries) {
        for (Entry entry: entries) {
            entry.hotelName = entry.hotelName + RandomStringUtils.randomAlphanumeric(10);
            homePage.createEntry(entry);
            entryList.add(entry);
        }
    }

    @Then("^entry should be added successfully$")
    public void entryShouldBeAddedSuccessfully() {
        for (Entry entry: entryList) {
            homePage.verifyEntryIsCreated(entry);
        }
    }

    @When("^user delete an entry$")
    public void userDeleteAnEntry(List<Entry> entries) {
        for (Entry entry: entries) {
            entry.hotelName = entry.hotelName + RandomStringUtils.randomAlphanumeric(10);
            homePage.createEntry(entry);
            entryList.add(entry);
        }

        entryShouldBeAddedSuccessfully();

        for (Entry entry: entryList) {
            homePage.deleteEntry(entry.hotelName);
        }
    }

    @Then("^entry should be deleted successfully$")
    public void entryShouldBeDeletedSuccessfully() {
        for(Entry entry: entryList) {
            homePage.verifyEntryIsDeleted(entry);
        }
    }
}
