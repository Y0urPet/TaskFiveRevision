package basicTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

public class DetailPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By addToCartButton = By.xpath("//button[contains(@class, 'btn btn-add-to-cart')]");
    private By closeModalButton = By.xpath("//button[contains(@class, 'btn btn-modal-close close')]");
    private By gotoCartPageIcon = By.id("show-your-cart");
    private By successModalText = By.xpath("//div[contains(@class, 'modal-text')]");
    private By oldPrice = By.xpath("//span[contains(@class, 'old')]");
    
    private static final int DEFAULT_WAIT_TIMEOUT = 20; 

    public DetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIMEOUT));
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
    
    public boolean verifySuccessAddingItemCartForValidation() {
    	boolean isSuccess = false;
    	WebElement successModal = waitForVisibilityOfElementLocated(successModalText);
    	SoftAssert softs = new SoftAssert();
    	softs.assertEquals(successModal.getText(),"Success add to cart", "Item failed to be added to cart");
    	if(successModal.getText().equals("Success add to cart")) {
    		isSuccess = true;
        }
    	return isSuccess;
    }
    
    public void verifySuccessAddingItemCart() {
    	WebElement successModal = waitForVisibilityOfElementLocated(successModalText);
    	SoftAssert softs = new SoftAssert();
    	softs.assertEquals(successModal.getText(),"Success add to cart", "Item failed to be added to cart");
    }
    
    private WebElement waitForVisibilityOfElementLocated(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
