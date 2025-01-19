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
	private DetailPage detailPage;
	private HomePage homePage;
	private CartPage cartPage;
	
	@BeforeClass
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "/Users/timothyandrian/Downloads/chromedriver-mac-arm64/chromedriver");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://periplus.com");
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		loginPage = new LoginPage(driver);
		detailPage = new DetailPage(driver);
		cartPage = new CartPage(driver);
		homePage = new HomePage(driver);
		
		homePage.clickSignInButton();
	    
	    loginPage.enterUsername(username);
	    loginPage.enterPassword(password);
	    loginPage.clickLoginButton();
	    loginPage.verifySuccessLogin();
	    
	    driver.get("https://periplus.com");
	}

	@Test(priority = 0)
	public void addItemToCart() throws InterruptedException {		
		for (int i = 0;i<3;i++) {			
			String discountItemXPath = "//div[contains(@class, 'single-product')]//span[contains(@style, 'color:#C4161C') and contains(text(), '%')]/ancestor::div[contains(@class, 'single-product')]//a";
            List<WebElement> discountItems = driver.findElements(By.xpath(discountItemXPath));
		 
            for(WebElement element: discountItems) {
            	if(!element.getText().isEmpty()) {
            		element.click();
            		break;
            	}
            }
            WebElement oldPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class, 'old')]")));
            String oldPriceExtract = cartPage.extractNumber(oldPrice.getText());
			String oldPriceWithoutComma = oldPriceExtract.replace(",", "");
			oldItemPrice = Integer.parseInt(oldPriceWithoutComma);						
            
		    
		    detailPage.clickAddToCart();	    
		    
		    detailPage.verifySuccessAddingItemCart();
		    
		    detailPage.clickCloseModal();
		    
		    driver.get("https://periplus.com");
		}
	}
	
	@Test(priority = 1)
	public void verifyItemInCart() throws InterruptedException {
		// Click Cart Icon in Nav Bar
		detailPage.gotoCartPage();
		// Verify Item have been Added in Cart
	    cartPage.verifyCartFilled();
	}
	
	@Test(priority = 2)
	public void verifySubtotalPrice() throws InterruptedException {
		WebElement subTotal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@id, 'sub_total')]")));
		String subTotalPrice = cartPage.extractNumber(subTotal.getText());
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
		cartPage.removeAllItemInCart();
		driver.quit();
	}
}
