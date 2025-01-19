package basicTest;

import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
	private WebDriver driver;
	private WebDriverWait wait;

    // Locators
    private By usernameField = By.name("email");
    private By passwordField = By.name("password");
    private By loginButton = By.id("button-login");
    private By welcomeText = By.xpath("//div[contains(@class, 'row row-account')]");
    
    private static final int DEFAULT_WAIT_TIMEOUT = 20; 

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIMEOUT));
    }
    
    private void waitForElementToBeClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    private void waitForPresenceOfAllElementsLocatedBy(By locator) {
    	wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
    
    private WebElement waitForVisibilityOfElementLocated(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
	// Methods
    public void enterUsername(String username) {
    	waitForVisibilityOfElementLocated(usernameField);
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
    	waitForVisibilityOfElementLocated(passwordField);
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLoginButton() {
    	waitForElementToBeClickable(loginButton);
        driver.findElement(loginButton).click();
    }
    
    public void verifySuccessLogin() {
    	WebElement welcom = waitForVisibilityOfElementLocated(welcomeText);
    	assertTrue(welcom.isDisplayed(), "Login failed! Personal Information Not Appear!");
    }
}
