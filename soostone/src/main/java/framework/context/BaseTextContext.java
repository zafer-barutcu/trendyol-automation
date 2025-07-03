package framework.context;

import framework.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static framework.core.ThreadLocalDriver.getDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intermediate context layer between {@link BasePage} and individual Page Object classes.
 * <p>
 * This class contains:
 * <ul>
 *   <li>Common locators and reusable utility methods shared across multiple page classes</li>
 *   <li>Generic helper functions for interacting with elements (e.g., waits, clicks, scrolling, switching tabs)</li>
 * </ul>
 * Designed to promote code reuse and maintain consistency
 */

public class BaseTextContext extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BaseTextContext.class);

    //@FindBy(css = "div.basket-item-count-container.visible")
    @FindBy(css = "div.basket-item-count")
    protected WebElement cartItemCount;
    protected final By productSearchBox = By.cssSelector("input[data-testid='suggestion']");
    protected final By productSearch = By.cssSelector("i[data-testid='search-icon']");
    protected final By myCartButton = By.xpath("//p[contains(@class, 'link-text') and contains(text(), 'Sepetim')]");
    protected Duration defaultWait = Duration.ofSeconds(30);

    protected void sendKeysTo(By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void clickElement(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, defaultWait);
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    protected void clickElement(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, defaultWait);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    protected WebElement explicitWaitForElement(By by, Duration time) {
        WebDriverWait wait = new WebDriverWait(driver, time);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected WebElement explicitWaitForElement(By by) {
        return explicitWaitForElement(by, defaultWait);
    }

    protected void waitThenClickElement(By by, int time) {
        waitThenClickElement(by, Duration.ofSeconds(time));
    }

    protected void waitThenClickElement(By by, Duration time) {
        WebDriverWait wait = new WebDriverWait(driver, time);
        explicitWaitForElement(by, time);
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    protected void switchFocusToNewTab() {
        WebDriver driver = getDriver();
        List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windowHandles.get(windowHandles.size() - 1));
    }

    protected void clickMyCartButton() {
        explicitWaitForElement(myCartButton);
        clickElement(myCartButton);
    }

    protected void waitUntilCartItemCountIs(int expectedCount, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds));

        wait.until(driver -> {
            try {
                scrollToElement(cartItemCount);
                String text = cartItemCount.getText().trim();
                int count = Integer.parseInt(text);
                return count >= expectedCount;
            } catch (Exception e) {
                return false;
            }
        });
        log.info("Cart icon shows at least {} item(s)", expectedCount);
    }

    protected void scrollToCenter(JavascriptExecutor executor, WebElement element) {
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'})", element);
    }

    protected void scrollToElement(WebElement element) {
        scrollToCenter((JavascriptExecutor) getDriver(), element);
    }

    protected void waitUntilVisibleAndClickWithScroll(WebElement element) {
        new WebDriverWait(driver, defaultWait)
                .until(ExpectedConditions.visibilityOf(element));
        scrollToElement(element);
        element.click();
    }

    public enum ProductField {
        NAME,
        PRICE,
        AVAILABILITY,
    }

    public Map<ProductField, Object> getProductDetails(String name, String price, boolean availability) {
        Map<ProductField, Object> details = new EnumMap<>(ProductField.class);
        details.put(ProductField.NAME, name);
        details.put(ProductField.PRICE, price);
        details.put(ProductField.AVAILABILITY, availability);
        return details;
    }

    protected void waitUntilValueEquals(By locator, String expectedValue, Duration timeout) {
        new WebDriverWait(driver, timeout)
                .until(driver -> {
                    WebElement element = driver.findElement(locator);
                    return expectedValue.equals(element.getAttribute("value"));
                });
    }


}
