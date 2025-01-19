package basicTest;

import static org.testng.Assert.assertEquals;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

public class CartPage {
	private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By removeButton = By.xpath("//a[contains(@class, 'btn btn-cart-remove')]");
    private By saveForLaterButton = By.xpath("//a[contains(@class, 'btn btn-cart-save')]");
    private By rowOfProduct = By.xpath("//div[contains(@class, 'row row-cart-product')]");
    private By removeSavedItemButton = By.xpath("//a[contains(@class, 'btn btn-save-remove')]");
    private By addBackToCart = By.xpath("//a[contains(@class, 'btn btn-move-cart')]");
    private By continueShoppingButton = By.xpath("//a[contains(@class, 'btn text-white')]");
    private By homeCarousel = By.xpath("//div[contains(@class, 'container container-home-carousel')]");
    private By updateButton = By.xpath("//input[contains(@class, 'btn bg-transparent text-dark')]");
    private By itemSpinner = By.xpath("//input[contains(@class, 'input-number text-center')]");
    private By checkOutButton = By.xpath("//div[contains(@class, 'button5')]");
    private By checkOutHeading = By.xpath("//div[contains(@class, 'checkout-heading')]");
    private By plusQuantityButton = By.xpath("//div[contains(@class, 'button plus')]");
    private By EmptyCartContent = By.xpath("//div[@id='content']//div[@class='content']");
    private By subTotalSpan = By.xpath("//span[contains(@id, 'sub_total')]");
    private By totalWithTaxSpan = By.xpath("(//span[contains(@id, 'sub_total')])[2]");
    private By itemPriceSpan = By.xpath("//div[@class='row row-cart-product']//div[@class='col-lg-10 col-9']//div[contains(text(), 'Rp')]");
    
    private static final int DEFAULT_WAIT_TIMEOUT = 20; 

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIMEOUT));
    }

    // Methods
    private WebElement waitForElementToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    private void waitForPresenceOfAllElementsLocatedBy(By locator) {
    	wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
    
    private WebElement waitForVisibilityOfElementLocated(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    public void clickRemoveButton() {
    	waitForElementToBeClickable(removeButton);
    	driver.findElement(removeButton).click();;
    }
    
    public void clickSaveForLater() {
    	waitForElementToBeClickable(saveForLaterButton);
    	driver.findElement(saveForLaterButton).click();
    }
    
    public void clickRemoveSavedItem() {
    	waitForElementToBeClickable(removeSavedItemButton);
    	driver.findElement(removeSavedItemButton).click();
    }
    
    public void clickAddToCart() {
    	waitForElementToBeClickable(addBackToCart);
    	driver.findElement(addBackToCart).click();
    }
    
    public void clickContinueShoppingButton() {
    	waitForElementToBeClickable(continueShoppingButton);
    	driver.findElement(continueShoppingButton).click();
    }
    
    public void clickUpdateButton() {
    	waitForVisibilityOfElementLocated(updateButton);
    	driver.findElement(updateButton).click();
    }
    
    public void clickCheckOutButton() {
    	waitForElementToBeClickable(checkOutButton);
    	driver.findElement(checkOutButton).click();
    }
    
    public void clickPlusQuantityButton() {
    	waitForElementToBeClickable(plusQuantityButton);
    	driver.findElement(plusQuantityButton).click();
    }
    
    public void removeAllItemInCart() {
    	waitForPresenceOfAllElementsLocatedBy(rowOfProduct);
    	waitForVisibilityOfElementLocated(removeButton);
    	List<WebElement> elements = driver.findElements(removeButton);
    	for(int i = 0;i<elements.size();i++) {
    		clickRemoveButton();
    	}
    }
    
    public void saveAllItemInCart() {
    	waitForPresenceOfAllElementsLocatedBy(rowOfProduct);
    	waitForVisibilityOfElementLocated(saveForLaterButton);
    	List<WebElement> elements = driver.findElements(saveForLaterButton);
    	for(int i = 0;i<elements.size();i++) {
    		clickSaveForLater();
    	}
    }
    
    public void removeAllSavedItemInCart() {
    	waitForPresenceOfAllElementsLocatedBy(rowOfProduct);
    	waitForVisibilityOfElementLocated(removeSavedItemButton);
    	List<WebElement> elements = driver.findElements(removeSavedItemButton);
    	for(int i = 0;i<elements.size();i++) {
    		clickRemoveSavedItem();
    	}
    }
    
    public void moveAllSavedItemBackToCart() {
    	waitForPresenceOfAllElementsLocatedBy(rowOfProduct);
    	waitForVisibilityOfElementLocated(removeSavedItemButton);
    	List<WebElement> elements = driver.findElements(removeSavedItemButton);
    	for(int i = 0;i<elements.size();i++) {
    		clickAddToCart();
    	}
    }
    
    public void verifyCartEmpty() {
    	WebElement verifyCartItem = waitForVisibilityOfElementLocated(EmptyCartContent);
    	assertEquals(verifyCartItem.isDisplayed(), true, "There is Item in Cart!");
    }
    
    public void verifyCartFilled() {
    	WebElement verifyCartItem = waitForVisibilityOfElementLocated(rowOfProduct);
    	assertEquals(verifyCartItem.isDisplayed(), true, "No Item in Cart!");
    }
    
    public void verifyItemHaveBeenSaved() {
    	WebElement removeSavedButton = waitForVisibilityOfElementLocated(removeSavedItemButton);
    	assertEquals(removeSavedButton.isDisplayed(), true, "NO ITEM HAVE BEEN SAVED");
    }
    
    public void verifyPageChange() {
    	WebElement homeCar = waitForVisibilityOfElementLocated(homeCarousel);
		assertEquals(homeCar.isDisplayed(),true,"Home Screen not Appear!");
    }
    
    public void verifyItemHaveBeenModified() {
    	
    	WebElement spinner = waitForVisibilityOfElementLocated(itemSpinner);
    	
    	String newQuantity = spinner.getDomAttribute("value"); 
    	
    	boolean isDifferent = false;
    	
    	if (Integer.parseInt(newQuantity) == 1) {
    		isDifferent = true;
    	}
    	SoftAssert sofS = new SoftAssert();
    	sofS.assertEquals(isDifferent, true, "The item is not modified");
    }
    
    public void verifyCheckOutPage() {
    	WebElement heading = waitForVisibilityOfElementLocated(checkOutHeading);
    	assertEquals(heading.isDisplayed(),true,"Checkout Screen Screen not Appear!");
    }
    
    public void verifyDifferencesQuantity(String quantityOne,String quantityTwo) {
    	boolean isDifferent = false;
    	quantityOne.replace(",", "");
    	quantityTwo.replace(",", "");
	    
	    if (Integer.parseInt(quantityOne) != Integer.parseInt(quantityTwo)) {
	    	isDifferent = true;
	    }
	    assertEquals(isDifferent, true, "The item is not modified");
    }
    
    public void verifyDifferencesPrice(String quantityOne,String quantityTwo) {
    	boolean isDifferent = true;
    	String pureNumberOne = extractNumber(quantityOne);
    	String pureNumberTwo = extractNumber(quantityTwo);
    	String numNoCommaOne = pureNumberOne.replace(",", "");
    	String numNoCommaTwo = pureNumberTwo.replace(",", "");
    	System.out.println(numNoCommaOne);
    	System.out.println(numNoCommaTwo);
	    
	    if (Integer.parseInt(numNoCommaOne) != Integer.parseInt(numNoCommaTwo)) {
	    	isDifferent = false;
	    }
	    assertEquals(isDifferent, true, "The item is not modified");
    }
    
    public String getSpinnerValue() {
    	WebElement inputNumber = waitForVisibilityOfElementLocated(itemSpinner);
	    String oldItemQuantity = inputNumber.getDomAttribute("value");
	    return oldItemQuantity;
    }
    
    public String getSubtotal() {
    	WebElement subtotal = waitForVisibilityOfElementLocated(subTotalSpan);
    	String subtotalText = subtotal.getText();
    	return subtotalText;
    }
    
    public String getItemPriceSpan() {
    	WebElement itemPrice = waitForVisibilityOfElementLocated(itemPriceSpan);
    	String itemPricetext = itemPrice.getText();
    	return itemPricetext;
    }
    
    public String extractNumber(String priceString) {
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
    
    public int getSumOfSubtotal() {
    	waitForVisibilityOfElementLocated(rowOfProduct);
    	List<WebElement> items = driver.findElements(rowOfProduct);
		int totalSubTotal = 0;
		
		for (int i = 0;i<items.size();i++) {
			WebElement itemPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='row row-cart-product'])[" +(i + 1) + "]//div[@class='col-lg-10 col-9']//div[contains(text(), 'Rp')]")));
			String itemPriceTotal = extractNumber(itemPrice.getText());
			String ItemPriceWithoutComma = itemPriceTotal.replace(",", "");
			totalSubTotal += Integer.parseInt(ItemPriceWithoutComma);
		}
		return totalSubTotal;
    }
    
    public void compareSubTotalWithMultipleitemPrice() {
		
		WebElement subTotal = waitForVisibilityOfElementLocated(subTotalSpan);
		String subTotalPrice = extractNumber(subTotal.getText());
		String subTotalPriceWithoutComa = subTotalPrice.replace(",", "");

		assertEquals(getSumOfSubtotal(),Integer.parseInt(subTotalPriceWithoutComa),"Price are not the same!");
    }
    
    public void compareSubtotalWithTotalWithTax() {
    	WebElement subTotal = wait.until(ExpectedConditions.visibilityOfElementLocated(subTotalSpan));
		String subTotalPrice = extractNumber(subTotal.getText());
		String subTotalPriceWithoutComa = subTotalPrice.replace(",", "");
		System.out.println("Subtotal: "+subTotalPriceWithoutComa);
		
		WebElement total = wait.until(ExpectedConditions.visibilityOfElementLocated(totalWithTaxSpan));
		String totalPrice = extractNumber(total.getText());
		String totalPriceWithoutComma = totalPrice.replace(",", "");
		System.out.println("Total: "+totalPriceWithoutComma);		

		assertEquals(Integer.parseInt(totalPriceWithoutComma),Integer.parseInt(subTotalPriceWithoutComa),"Price are not the same!");
    }
    
}
