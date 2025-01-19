package basicTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
	private WebDriver driver;

    // Locators
    private By usernameField = By.name("email");
    private By passwordField = By.name("password");
    private By loginButton = By.id("button-login");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

	// Methods
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLoginButton() {
        driver.findElement(loginButton).click();
    }
}
