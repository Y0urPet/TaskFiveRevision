package basicTest;

import static org.testng.Assert.assertEquals;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

public class HomePage {
	private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By previousButton = By.xpath("//div[contains(@class, 'owl-prev')]");
    private By firstActiveItem = By.xpath("//div[contains(@class, 'owl-stage')]//div[contains(@class, 'owl-item active')]//div[@class='single-product']//div[not(contains(@class, 'currently-unavailable'))]//a");
    private By signInButton = By.id("nav-signin-text");
    private By itemInCarousel = By.xpath("//div[contains(@class, 'single-product')]");
    private By choosenItem = By.xpath("//div[contains(@class, 'single-product') and not(.//div[contains(text(), 'CURRENTLY UNAVAILABLE')])]//a");
    private By discountItemInCarousel = By.xpath("//div[contains(@class, 'single-product')]//span[contains(@style, 'color:#C4161C') and contains(text(), '%')]/ancestor::div[contains(@class, 'single-product')]//a");
    
    private static final int DEFAULT_WAIT_TIMEOUT = 20; 
    private int total;

    public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIMEOUT));
    }

    // Methods
    private void waitForElementToBeClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    private void waitForVisibilityOfElementLocated(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    private void waitForPresenceOfAllElementsLocatedBy(By locator) {
    	wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
    
    public void clickPreviousButton() {
        waitForElementToBeClickable(previousButton);
        driver.findElement(previousButton).click();
    }
    
    public void clickTheFirstItem() {
    	waitForElementToBeClickable(firstActiveItem);
        driver.findElement(firstActiveItem).click();
    }
    
    public void clickSignInButton() {
    	waitForElementToBeClickable(signInButton);
        driver.findElement(signInButton).click();
    }
    
    public void chooseItem() throws InterruptedException {	
	    int counter = 0;
	    DetailPage detailPage = new DetailPage(driver);

	    while (counter < 3) {
	        waitForPresenceOfAllElementsLocatedBy(itemInCarousel);

	        List<WebElement> items = driver.findElements(choosenItem);

	        for (WebElement element : items) {
	            if (counter >= 3) {
	                break;
	            }
	            if (!element.getText().isEmpty()) {
	                element.click();

	                detailPage.clickAddToCart();

	                if(detailPage.verifySuccessAddingItemCartForValidation()) {
	                	counter++;	                	
	                }

	                detailPage.clickCloseModal();

	                driver.get("https://periplus.com");

	                waitForPresenceOfAllElementsLocatedBy(itemInCarousel);
	                int randNumber = (int)(Math.random() * (7 - 1) + 1);
        			for (int j = 0;j<randNumber;j++) {
        				clickPreviousButton();				
        			}
	                break;
	            }
	        }
	    }
	}    
    
}
