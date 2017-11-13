package pageobjects;

import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import world.Entry;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class HomePage {

    //region Page Elements

    private WebDriver driver;

    @FindBy(linkText = "Login")
    private WebElement loginLink = null;

    @FindBy(id = "username")
    private WebElement userName = null;

    @FindBy(id = "password")
    private WebElement password = null;

    @FindBy(id = "doLogin")
    private WebElement loginButton = null;

    @FindBy(id = "hotelName")
    private WebElement hotelName = null;

    @FindBy(id = "address")
    private WebElement address = null;

    @FindBy(id = "owner")
    private WebElement owner = null;

    @FindBy(id = "phone")
    private WebElement phoneNumber = null;

    @FindBy(id = "email")
    private WebElement email = null;

    @FindBy(id = "createHotel")
    private WebElement create = null;

    private By tableRows = By.cssSelector(".row.detail");

    private By hotelRow = By.xpath(".//div[@class='hotelRow']/div[1]");

    //endregion

    //region Methods

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        loginLink.click();
        userName.sendKeys(username);
        this.password.sendKeys(password);
        loginButton.click();
    }

    public void createEntry(Entry entry) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hotelName")));

        hotelName.sendKeys(entry.hotelName);
        address.sendKeys(entry.address);
        owner.sendKeys(entry.owner);
        phoneNumber.sendKeys(entry.phoneNumber);
        email.sendKeys(entry.email);
        create.click();
    }

    public void deleteEntry(String hotelName) {
        List<WebElement> entryRows = driver.findElements(tableRows);
        if (entryRows.isEmpty())
            fail("No entry is present to delete");

        Collection<WebElement> rows = Collections2.filter(entryRows, row -> row.findElement(By.xpath(".//div[@class='hotelRow']")).getText().contains(hotelName));
        if (rows.isEmpty()) {
            fail("Expected entry with hotel name '"+ hotelName + "' is not found");
        }
        Iterables.get(rows, 0).findElement(By.xpath(".//div[2]/span")).click();
    }

    public void verifyEntryIsCreated(final Entry entry) {
        sleep();
        List<WebElement> entryRows = driver.findElements(tableRows);
        if (entryRows.isEmpty())
            fail("Entries are not added successfully");

        Optional<WebElement> elements = entryRows.stream().filter(row -> row.findElement(hotelRow).getText().equals(entry.hotelName)).findAny();

        if (!elements.isPresent()) {
            fail("Expected entry with hotel name '"+ entry.hotelName + "' is not found");
        }
    }

    public void verifyEntryIsDeleted(final Entry entry) {
        sleep();
        List<WebElement> entryRows = driver.findElements(tableRows);
        if (entryRows.isEmpty()) {
            assertTrue(true);
            return;
        }

        Optional<WebElement> elements = entryRows.stream().filter(row -> row.findElement(hotelRow).getText().equals(entry.hotelName)).findAny();
        if (elements.isPresent()) {
            fail("Expected entry with hotel name '"+ entry.hotelName + "' is present");
        }
    }

    private void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //todo
        }
    }

    //endregion
}
