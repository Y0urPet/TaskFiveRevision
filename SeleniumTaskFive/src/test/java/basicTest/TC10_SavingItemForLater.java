package basicTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class TC10_SavingItemForLater {
	private WebDriver driver;
	private WebDriverWait wait;
	private String username = "taskfivetasktimothy@gmail.com";
	private String password = "timothytask555";
	private LoginPage loginPage;
	
	@BeforeClass
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "/Users/timothyandrian/Downloads/chromedriver-mac-arm64/chromedriver");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://periplus.com");
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		loginPage = new LoginPage(driver);
		
		WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-signin-text")));
	    signInButton.click();
	    
	    loginPage.enterUsername(username);
	    loginPage.enterPassword(password);
	    loginPage.clickLoginButton();
	    
	    WebElement welcomeMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'row row-account')]")));

	    assertTrue(welcomeMessage.isDisplayed(), "Login failed! Personal Information Not Appear!");
	    
	    driver.get("https://periplus.com");
	}

	@Test(priority = 0)
	public void chooseItem() throws InterruptedException {	
	    int counter = 0;

	    while (counter < 3) {
	        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'single-product')]")));

	        List<WebElement> items = driver.findElements(By.xpath("//div[contains(@class, 'single-product') and not(.//div[contains(text(), 'CURRENTLY UNAVAILABLE')])]//a"));

	        for (WebElement element : items) {
	            if (counter >= 3) {
	                break;
	            }
	            if (!element.getText().isEmpty()) {
	                System.out.println("Adding item: " + element.getText());

	                element.click();

	                WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
	                    By.xpath("//button[contains(@class, 'btn btn-add-to-cart')]")));
	                addToCartButton.click();

	                WebElement modalText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'modal-text')]")));
	                SoftAssert softS = new SoftAssert();
	                softS.assertEquals(modalText.getText(), "Success add to cart", "Item failed to be added to cart");
	                if(modalText.getText().equals("Success add to cart")) {
	                	counter++;	                	
	                }

	                WebElement closeModalButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn btn-modal-close close')]")));
	                closeModalButton.click();

	                driver.get("https://periplus.com");

	                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'single-product')]")));
	                int randNumber = (int)(Math.random() * (7 - 1) + 1);
        			WebElement previousButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'owl-prev')]")));
        			for (int j = 0;j<randNumber;j++) {
        				previousButton.click();				
        			}
	                break;
	            }
	        }
	    }
	}

	
	@Test(priority = 1)
	public void verifyItemInCart() throws InterruptedException {
		WebElement cartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("show-your-cart")));
		cartButton.click();
	    WebElement verifyCartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'row row-cart-product')]")));
	    assertEquals(verifyCartItem.isDisplayed(), true, "No Item in Cart!");
	}
	
	@Test(priority = 2)
	public void addItemToSaveForLater() throws InterruptedException {
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'row row-cart-product')]")));
		List<WebElement> elements = driver.findElements(By.xpath("//a[contains(@class, 'btn btn-cart-save')]"));
		for(int i = 0;i<elements.size();i++) {
			WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'btn btn-cart-save')]")));
			saveButton.click();
		}
	}
	
	@Test(priority = 3)
	public void verifyItemHaveBeenSaved() throws InterruptedException {
		WebElement removeSaveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'btn btn-save-remove')]")));
		assertEquals(removeSaveButton.isDisplayed(), true, "NO ITEM HAVE BEEN SAVED");
	}

	@AfterClass
	public void tearDown() {
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'row row-cart-product')]")));
		List<WebElement> elements = driver.findElements(By.xpath("//a[contains(@class, 'btn btn-save-remove')]"));
		for(int i = 0;i<elements.size();i++) {
			WebElement removeSaveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@class, 'btn btn-save-remove')]")));
			removeSaveButton.click();
		}
		driver.quit();
	}
}
