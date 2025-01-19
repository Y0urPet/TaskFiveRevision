package basicTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    public String extractNumber(String priceString) {
		String result = "";

		String regex = "Rp\\s+(\\d+(?:,\\d+)*)"; 

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(priceString);

        if (matcher.find()) {
            String price = matcher.group(1); 
            result = price;
        } else {
            System.out.println("Price not found in the string.");
        }
        return result;
	}
}
