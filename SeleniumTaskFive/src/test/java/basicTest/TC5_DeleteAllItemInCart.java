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

public class TC5_DeleteAllItemInCart {
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
		for (int i = 0;i<3;i++) {
			int randNumber = (int)(Math.random() * (7 - 1) + 1);
	//		owl-prev
			WebElement previousButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'owl-prev')]")));
			for (int j = 0;j<randNumber;j++) {
				previousButton.click();				
			}
			
			// currently-unavailable
		    WebElement firstActiveItem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'owl-stage')]//div[contains(@class, 'owl-item active')]//div[@class='single-product']//div[not(contains(@class, 'currently-unavailable'))]//a")));
		    firstActiveItem.click();
		    
			// Click add to cart button
		    WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn btn-add-to-cart')]")));
		    addToCartButton.click();	    
		    
		    // Checking if Item was added successfully to the cart
		    WebElement modalText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'modal-text')]")));
		    assertEquals(modalText.getText(),"Success add to cart", "Item failed to be added to cart");
		    
		    // Click close icon in the appearing modal
		    WebElement closeModalButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@class, 'btn btn-modal-close close')]")));
		    closeModalButton.click();
		    driver.get("https://periplus.com");
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
	public void deleteItemInCart() throws InterruptedException {
		List<WebElement> elements = driver.findElements(By.xpath("//a[contains(@class, 'btn btn-cart-remove')]"));
		for(int i = 0;i<elements.size();i++) {
			WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'btn btn-cart-remove')]")));
			removeButton.click();
		}
	}
	
	@Test(priority = 3)
	public void verifyCartEmpty() throws InterruptedException {
		WebElement contentTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='content']//div[@class='content']")));
		assertEquals(contentTitle.isDisplayed(), true, "Cart is Not Empty!");		
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}
}
