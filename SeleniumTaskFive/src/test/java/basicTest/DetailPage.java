package basicTest;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DetailPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By addToCartButton = By.xpath("//button[contains(@class, 'btn btn-add-to-cart')]");
    private By closeModalButton = By.xpath("//button[contains(@class, 'btn btn-modal-close close')]");
    private By gotoCartPageIcon = By.id("show-your-cart");
    private static final int DEFAULT_WAIT_TIMEOUT = 20; 

    public DetailPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIMEOUT));
    }

    // Methods
    public void clickAddToCart() {
        waitForElementToBeClickable(addToCartButton);
        driver.findElement(addToCartButton).click();
    }

    public void clickCloseModal() {
        waitForElementToBeClickable(closeModalButton);
        driver.findElement(closeModalButton).click();
    }

    public void gotoCartPage() {
        waitForElementToBeClickable(gotoCartPageIcon);
        driver.findElement(gotoCartPageIcon).click();
    }

    private void waitForElementToBeClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
}
