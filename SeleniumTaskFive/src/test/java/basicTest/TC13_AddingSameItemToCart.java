package basicTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.List;

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

public class TC13_AddingSameItemToCart {
	private WebDriver driver;
	private WebDriverWait wait;
	private String username = "taskfivetasktimothy@gmail.com";
	private String password = "timothytask555";
	private LoginPage loginPage;
	
	@BeforeClass
	public void setUp() {
		// setting up the driver
		// Please Update to your own path to ChromeDriver
		System.setProperty("webdriver.chrome.driver", "/Users/timothyandrian/Downloads/chromedriver-mac-arm64/chromedriver");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://periplus.com");
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		loginPage = new LoginPage(driver);
		//LOGIN TEST
		
		// Click Sign In
		WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-signin-text")));
	    signInButton.click();
	    
	    // Enter email
	    loginPage.enterUsername(username);
	    loginPage.enterPassword(password);
	    loginPage.clickLoginButton();
	    
	    // Wait for a successful login indicator (e.g., welcome message) to appear
	    WebElement welcomeMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'row row-account')]")));

	    // Assertion to verify successful login
	    assertTrue(welcomeMessage.isDisplayed(), "Login failed! Personal Information Not Appear!");
	    
	    // Go to Home Page
	    driver.get("https://periplus.com");
	}
	
	@Test(priority = 0)
	public void addItemToCart() throws InterruptedException {
		for (int i = 0;i<2;i++) {
			driver.get("https://www.periplus.com/p/9781599327853/embracing-progress-next-steps-for-the-future-of-work");
			// Wait for the Add to Cart button and click it
	        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn btn-add-to-cart')]")));
	        addToCartButton.click();

	        // Verify success modal
	        WebElement modalText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'modal-text')]")));
	        assertEquals(modalText.getText(), "Success add to cart", "Item failed to be added to cart");
	        
	        // Close the modal
	        WebElement closeModalButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn btn-modal-close close')]")));
	        closeModalButton.click();
		}
	}

	
	@Test(priority = 1)
	public void verifyItemInCart() throws InterruptedException {
		// Click Cart Icon in Nav Bar
		WebElement cartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("show-your-cart")));
		cartButton.click();
		// Verify Item have been Added in Cart
	    WebElement verifyCartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'row row-cart-product')]")));
	    assertEquals(verifyCartItem.isDisplayed(), true, "No Item in Cart!");
	}
	
	@Test(priority = 2)
	public void verifyItemBeenModified() throws InterruptedException {
		driver.get("https://www.periplus.com/checkout/cart");
		
		WebElement inputNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@class, 'input-number text-center')]")));
		String newQuantity = inputNumber.getDomAttribute("value"); 
		
	    boolean isDifferent = false;
	    
	    if (Integer.parseInt(newQuantity) == 1) {
	    	isDifferent = true;
	    }
	    SoftAssert sofS = new SoftAssert();
	    sofS.assertEquals(isDifferent, true, "The item is not modified");
	}
	

	@AfterClass
	public void tearDown() {
		// Delete all item
		List<WebElement> elements = driver.findElements(By.xpath("//a[contains(@class, 'btn btn-cart-remove')]"));
		for(int i = 0;i<elements.size();i++) {
			WebElement removeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@class, 'btn btn-cart-remove')]")));
			removeButton.click();
		}
		driver.quit();
	}
}
