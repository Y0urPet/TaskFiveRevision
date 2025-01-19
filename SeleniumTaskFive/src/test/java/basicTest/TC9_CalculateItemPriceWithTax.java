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

public class TC9_CalculateItemPriceWithTax {
	private WebDriver driver;
	private WebDriverWait wait;
	private String username = "taskfivetasktimothy@gmail.com";
	private String password = "timothytask555";
	private LoginPage loginPage;
	
	String extractNumber(String priceString) {
		String result = "";
        String regex = "Rp\\s+(\\d+(?:,\\d+)*)"; 

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(priceString);

        if (matcher.find()) {
            String price = matcher.group(1); 
            result = price;
        } else {
            System.out.println("Price not found in the string.");
        }
        
        return result;
	}

	
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
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'single-product')]")));
		// Click the first Item
	    List<WebElement> items = driver.findElements(By.xpath("//div[contains(@class, 'single-product') and not(.//div[contains(text(), 'CURRENTLY UNAVAILABLE')])]//a"));
	    for(WebElement element: items) {
        	if(!element.getText().equals("")) {
        		element.click();
        		break;
        	}
        }
	 }
	
	@Test(priority = 1)
	public void addItemToCart() throws InterruptedException {
	    WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn btn-add-to-cart')]")));
	    addToCartButton.click();	    
	    
	    WebElement modalText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'modal-text')]")));
	    assertEquals(modalText.getText(),"Success add to cart", "Item failed to be added to cart");
	    
	    WebElement closeModalButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@class, 'btn btn-modal-close close')]")));
	    closeModalButton.click();
	}
	
	@Test(priority = 2)
	public void verifyItemInCart() throws InterruptedException {
		WebElement cartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("show-your-cart")));
		cartButton.click();
	    WebElement verifyCartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'row row-cart-product')]")));
	    assertEquals(verifyCartItem.isDisplayed(), true, "No Item in Cart!");
	}
	
	@Test(priority = 3)
	public void verifySubtotalPrice() throws InterruptedException {
		WebElement subTotal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@id, 'sub_total')]")));
		String subTotalPrice = extractNumber(subTotal.getText());
		String subTotalPriceWithoutComa = subTotalPrice.replace(",", "");
		System.out.println("Subtotal: "+subTotalPriceWithoutComa);
		
		WebElement total = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[contains(@id, 'sub_total')])[2]")));
		String totalPrice = extractNumber(total.getText());
		String totalPriceWithoutComma = totalPrice.replace(",", "");
		System.out.println("Total: "+totalPriceWithoutComma);
		
		boolean isTheSame = false;
		if (Integer.parseInt(subTotalPriceWithoutComa) == Integer.parseInt(totalPriceWithoutComma)) {
			isTheSame = true;
		}
		
		assertEquals(isTheSame,true,"Price are not the same!");
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
