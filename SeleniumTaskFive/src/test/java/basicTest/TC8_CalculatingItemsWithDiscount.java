package basicTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TC8_CalculatingItemsWithDiscount {	
	private WebDriver driver;
	private WebDriverWait wait;
	private String username = "taskfivetasktimothy@gmail.com";
	private String password = "timothytask555";
	private int oldItemPrice = 0;
	private int newItemPrice = 0;
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
	public void addItemToCart() throws InterruptedException {		
		for (int i = 0;i<3;i++) {			
			String discountItemXPath = "//div[contains(@class, 'single-product')]//span[contains(@style, 'color:#C4161C') and contains(text(), '%')]/ancestor::div[contains(@class, 'single-product')]//a";
            List<WebElement> discountItems = driver.findElements(By.xpath(discountItemXPath));
		 
            for(WebElement element: discountItems) {
            	if(!element.getText().equals("")) {
            		element.click();
            		break;
            	}
            }
            WebElement oldPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class, 'old')]")));
            String oldPriceExtract = loginPage.extractNumber(oldPrice.getText());
			String oldPriceWithoutComma = oldPriceExtract.replace(",", "");
			oldItemPrice = Integer.parseInt(oldPriceWithoutComma);						
            
		    
		    WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn btn-add-to-cart')]")));
		    addToCartButton.click();	    
		    
		    WebElement modalText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'modal-text')]")));
		    assertEquals(modalText.getText(),"Success add to cart", "Item failed to be added to cart");
		    
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
		WebElement subTotal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@id, 'sub_total')]")));
		String subTotalPrice = loginPage.extractNumber(subTotal.getText());
		String subTotalPriceWithoutComa = subTotalPrice.replace(",", "");
		System.out.println("Subtotal: "+subTotalPriceWithoutComa);
		System.out.println("OldPrice: "+oldItemPrice);
		
		boolean isTheDifference = false;
		if (Integer.parseInt(subTotalPriceWithoutComa) != oldItemPrice) {
			isTheDifference = true;
		}
		assertEquals(isTheDifference, true, "Price are not the same!");
	}

	@AfterClass
	public void tearDown() {
		List<WebElement> elements = driver.findElements(By.xpath("//a[contains(@class, 'btn btn-cart-remove')]"));
		for(int i = 0;i<elements.size();i++) {
			WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'btn btn-cart-remove')]")));
			removeButton.click();
		}
		driver.quit();
	}
}
