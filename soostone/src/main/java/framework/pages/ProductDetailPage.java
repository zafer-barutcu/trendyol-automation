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
import static framework.utils.CustomBy.buttonText;


public class ProductDetailPage extends BaseTextContext {


    private static final Logger log = LoggerFactory.getLogger(ProductDetailPage.class);
    private final By productNameLocator = By.xpath("//h1[contains(@class,'pr-new-br')]/span");
    private final By productPriceLocator = By.xpath("//div[@class='product-price-container']//div[contains(@class,'pr-bx-w')]//span[@class='prc-dsc']");
    private final By popupButtonLocator = By.xpath("//button[contains(text(), 'Anladım')]");
    int timeout = 10;
    int count = 1;

    private String getProductName() {
        return explicitWaitForElement(productNameLocator).getText().trim();
    }

    private String getProductPrice() {
        return explicitWaitForElement(productPriceLocator).getText().trim();
    }

    private boolean isProductAvailable() {
        try {
            WebElement button = explicitWaitForElement(buttonText("Sepete Ekle"));
            return button.isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private void waitForProductDetailPageToLoad() {
        explicitWaitForElement(productNameLocator);
    }

    public Map<ProductField, Object> getProductDetails() {
        return getProductDetails(getProductName(), getProductPrice(), isProductAvailable());
    }

    public void dismissLocationPopupIfVisible() {
        try {
            switchFocusToNewTab();
            String currentUrl = driver.getCurrentUrl();
            log.debug("Current URL after tab switch: {} ",currentUrl);

            WebElement popup = explicitWaitForElement(popupButtonLocator, Duration.ofSeconds(5));
            waitUntilVisibleAndClickWithScroll(popup);
            log.info("Location popup closed");
            waitForProductDetailPageToLoad();
        } catch (TimeoutException ignored) {
            log.info("No location popup appeared");
        }
    }

    public void addToCart() {
        WebElement addToCartBtn = explicitWaitForElement(buttonText("Sepete Ekle"));
        addToCartBtn.click();
        log.info("Product added to cart");
    }

     public void verifyProductAddedToCart() {
        waitUntilCartItemCountIs(count, timeout);
    }

     public void goToCartPage(){
        clickMyCartButton();
    }

















}
