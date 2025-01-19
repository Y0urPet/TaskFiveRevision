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

public class TC7_CalculatingMultipleItemSubtotal {
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
	public void verifySubtotalPrice() throws InterruptedException {
		List<WebElement> items = driver.findElements(By.xpath("//div[@class='row row-cart-product']"));
		int totalSubTotal = 0;
		
		for (int i = 0;i<items.size();i++) {
//			wait.until(ExpectedConditions.visibilityOf(items[i]));
			WebElement itemPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='row row-cart-product'])[" +(i + 1) + "]//div[@class='col-lg-10 col-9']//div[contains(text(), 'Rp')]")));
			String itemPriceTotal = extractNumber(itemPrice.getText());
			String ItemPriceWithoutComma = itemPriceTotal.replace(",", "");
			totalSubTotal += Integer.parseInt(ItemPriceWithoutComma);
		}
		
		// sub_total
		WebElement subTotal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@id, 'sub_total')]")));
		String subTotalPrice = extractNumber(subTotal.getText());
		String subTotalPriceWithoutComa = subTotalPrice.replace(",", "");
		
		boolean isTheSame = false;
		if (Integer.parseInt(subTotalPriceWithoutComa) == totalSubTotal) {
			isTheSame = true;
		}
		assertEquals(isTheSame,true,"Price are not the same!");
	}

	@AfterClass
	public void tearDown() {
		// Delete all item
		List<WebElement> elements = driver.findElements(By.xpath("//a[contains(@class, 'btn btn-cart-remove')]"));
		for(int i = 0;i<elements.size();i++) {
			WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'btn btn-cart-remove')]")));
			removeButton.click();
		}
		driver.quit();
	}
	String extractNumber(String priceString) {
		String result = "";
		// Regular expression to match the price
		String regex = "Rp\\s+(\\d+(?:,\\d+)*)"; 
		
		// Create a Pattern object
		Pattern pattern = Pattern.compile(regex);
		
		// Create a Matcher object
		Matcher matcher = pattern.matcher(priceString);
		
		// Find the match
		if (matcher.find()) {
			// Extract the price
			String price = matcher.group(1); 
			result = price;
		} else {
			System.out.println("Price not found in the string.");
		}
		
		return result;
	}
}
