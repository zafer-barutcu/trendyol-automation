package framework.pages;

import framework.context.BaseTextContext;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.Map;
import java.util.NoSuchElementException;


public class ProductDetailPage extends BaseTextContext {


    private static final Logger log = LoggerFactory.getLogger(ProductDetailPage.class);

    //private final By productNameLocator = By.xpath("//h1[contains(@class,'pr-new-br')]/span");
    private final By productName = By.xpath("//h1[@data-testid='product-title']");

    //private final By productPriceLocator = By.xpath("//div[@class='product-price-container']//div[contains(@class,'pr-bx-w')]//span[@class='prc-dsc']");
    private final By productPrice = By.cssSelector("div[data-testid='price'] span.price-view-original");
    private final By popupButton = By.xpath("//button[contains(text(), 'Anladım')]");
    private final By addToCartBtn = By.xpath("//button[@data-testid='add-to-cart-button']//span[@class='add-to-cart-button-text']");
    int timeout = 10;
    int count = 1;

    private String getProductName() {
        return explicitWaitForElement(productName).getText().trim();
    }

    private String getProductPrice() {
        return explicitWaitForElement(productPrice).getText().trim();
    }

    private boolean isProductAvailable() {
        try {
            WebElement button = explicitWaitForElement(addToCartBtn);
            String buttonText = button.getText().trim();
            return button.isEnabled() && buttonText.equalsIgnoreCase("Sepete Ekle");
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private void waitForProductDetailPageToLoad() {
        explicitWaitForElement(productName);
    }

    public Map<ProductField, Object> getProductDetails() {
        return getProductDetails(getProductName(), getProductPrice(), isProductAvailable());
    }

    public void dismissLocationPopupIfVisible() {
        try {
            switchFocusToNewTab();
            String currentUrl = driver.getCurrentUrl();
            log.debug("Current URL after tab switch: {} ",currentUrl);

            WebElement popup = explicitWaitForElement(popupButton, Duration.ofSeconds(5));
            waitUntilVisibleAndClickWithScroll(popup);
            log.info("Location popup closed");
            waitForProductDetailPageToLoad();
        } catch (TimeoutException ignored) {
            log.info("No location popup appeared");
        }
    }

    public void addToCartFromDetailPage() {
        WebElement addToCartButton = explicitWaitForElement(addToCartBtn);
        addToCartButton.click();
        log.info("Product added to cart");
    }

     public void verifyProductAddedToCart() {
        waitUntilCartItemCountIs(count, timeout);
    }

     public void goToCartPage(){
        clickMyCartButton();
    }

















}
