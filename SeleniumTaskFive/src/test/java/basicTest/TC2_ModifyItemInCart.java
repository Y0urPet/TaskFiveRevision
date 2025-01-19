package basicTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.Duration;

import org.jspecify.annotations.Nullable;
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

public class TC2_ModifyItemInCart {
	private WebDriver driver;
	private WebDriverWait wait;
	private String username = "taskfivetasktimothy@gmail.com";
	private String password = "timothytask555";
	private String oldItemQuantity = "";
	private String newQuantity = "";
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
		WebElement previousButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'owl-prev')]")));
		previousButton.click();
		
	    WebElement firstActiveItem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'owl-stage')]//div[contains(@class, 'owl-item active')]//div[@class='single-product']//div[not(contains(@class, 'currently-unavailable'))]//a")));
	    firstActiveItem.click();
	 }
	
	@Test(priority = 1)
	public void addItemToCart() throws InterruptedException {
	    WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn btn-add-to-cart')]")));
	    addToCartButton.click();	    
	    
	    WebElement modalText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'modal-text')]")));
	    assertEquals(modalText.getText(),"Success add to cart", "Item failed to be added to cart");
	    
	    WebElement closeModalButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@class, 'btn btn-modal-close close')]")));
	    closeModalButton.click();
	    
	    WebElement cartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("show-your-cart")));
	    cartButton.click();
	    
	    WebElement inputNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@class, 'input-number text-center')]")));
	    oldItemQuantity = inputNumber.getDomAttribute("value");
	    System.out.println("Ini total: "+oldItemQuantity);
	}
	
	@Test(priority = 2)
	public void modifyItemInCart() throws InterruptedException{
		WebElement plusButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'button plus')]")));
		plusButton.click();
	}
	
	@Test(priority = 3)
	public void verifyItemInCart() throws InterruptedException {
	    WebElement verifyCartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'row row-cart-product')]")));
	    assertEquals(verifyCartItem.isDisplayed(), true, "No Item in Cart!");
	}
	
	@Test(priority = 4)
	public void verifyItemBeenModified() throws InterruptedException {
		driver.get("https://www.periplus.com/checkout/cart");
		
		WebElement inputNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@class, 'input-number text-center')]")));
		newQuantity = inputNumber.getDomAttribute("value"); 
		System.out.println("Ini total Baru: "+newQuantity);
		
	    boolean isDifferent = false;
	    
	    if (Integer.parseInt(oldItemQuantity) != Integer.parseInt(newQuantity)) {
	    	isDifferent = true;
	    }
	    assertEquals(isDifferent, true, "The item is not modified");
	}

	@AfterClass
	public void tearDown() {
		WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'btn btn-cart-remove')]")));
		removeButton.click();
		driver.quit();
	}
}
