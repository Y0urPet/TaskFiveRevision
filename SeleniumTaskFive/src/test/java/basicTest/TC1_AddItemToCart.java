package basicTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TC1_AddItemToCart {
	private WebDriver driver;
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
	    
	    detailPage.gotoCartPage();
	}
	
	@Test(priority = 2)
	public void verifyItemInCart() throws InterruptedException {
		cartPage.verifyCartEmpty();
	}

	@AfterClass
	public void tearDown() {
		cartPage.clickRemoveButton();
		driver.quit();
	}
}
