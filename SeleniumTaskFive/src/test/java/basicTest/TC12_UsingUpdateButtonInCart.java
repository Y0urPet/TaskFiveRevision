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

public class TC12_UsingUpdateButtonInCart {
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
		// Wait for the Add to Cart button and click it
        detailPage.clickAddToCart();

        // Verify success modal
        detailPage.verifySuccessAddingItemCart();

        // Close the modal
        detailPage.clickCloseModal();
        
        detailPage.gotoCartPage();
	}
	
	@Test(priority = 2)
	public void updateCart() throws InterruptedException {
		cartPage.clickUpdateButton();
	}
	
	@AfterClass
	public void tearDown() {
		cartPage.removeAllItemInCart();
		driver.quit();
	}
}
