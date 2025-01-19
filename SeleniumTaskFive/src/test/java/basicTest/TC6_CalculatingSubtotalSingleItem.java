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

public class TC6_CalculatingSubtotalSingleItem {
	private WebDriver driver;
	private WebDriverWait wait;
	private String username = "taskfivetasktimothy@gmail.com";
	private String password = "timothytask555";
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
	public void chooseItem() throws InterruptedException {
		homePage.clickPreviousButton();
		homePage.clickTheFirstItem();
	 }
	
	@Test(priority = 1)
	public void addItemToCart() throws InterruptedException {
		detailPage.clickAddToCart();    
	    
	    detailPage.verifySuccessAddingItemCart();
	    
	    detailPage.clickCloseModal();
	}
	
	@Test(priority = 2)
	public void verifyItemInCart() throws InterruptedException {
		detailPage.gotoCartPage();
	    cartPage.verifyCartFilled();
	}
	
	@Test(priority = 3)
	public void verifySubtotalPrice() throws InterruptedException {
		String subTotal = cartPage.getSubtotal();
		String itemPrice = cartPage.getItemPriceSpan();
		
		cartPage.verifyDifferencesPrice(subTotal, itemPrice);
	}

	@AfterClass
	public void tearDown() {
		cartPage.removeAllItemInCart();
		driver.quit();
	}
}
