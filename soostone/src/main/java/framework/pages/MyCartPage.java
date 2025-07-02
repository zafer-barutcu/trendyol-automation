package framework.pages;

import framework.context.BaseTextContext;
import framework.utils.Retry;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class MyCartPage extends BaseTextContext {

    private static final Logger log = LoggerFactory.getLogger(MyCartPage.class);

    private final By myCartPageHeader = By.xpath("//div[contains(@class, 'pb-header') and contains(text(), 'Sepetim')]");
    private final By closeLocatorPopUp = By.xpath("//button[contains(text(), 'Anladım')]");
    private final By itemNameInCart = By.cssSelector(".pb-basket-item .pb-basket-item-details > a > p");
    private final By itemPriceInCart = By.cssSelector(".pb-basket-item-price");
    private final By itemQuantityInCart = By.xpath("//input[contains(@class,'counter-content') and @aria-label='Ürün adedi']");
    private final By increaseItemQuantityButton = By.xpath("//button[@aria-label='Ürün adedi arttırma']");
    private final By decreaseItemQuantityButton = By.xpath("//button[@aria-label='Ürün adedi azaltma']");
    private final By deleteItemButton = By.xpath("//button[@aria-label='Ürünü sepetten çıkartma']//i");
    private final By removedItemMessage = By.xpath("//div[contains(@class, 'pb-basket-item-removed-item-information-label')]//p");
    private final By productTotalPrice = By.xpath("//li/span[.='Ürünün Toplamı']/following-sibling::strong");
    private final By shipmentTotalPrice = By.xpath("//li/span[.='Kargo Toplam']/following-sibling::strong");
    private final By cartTotalPrice = By.xpath("//div[@class='pb-summary-total-price discount-active']");



    public boolean isAtCartPage() {
        return explicitWaitForElement(myCartPageHeader) != null;
    }

    public void checkAndDismissIfLocatorPopupInCartPageIsVisible() {
        try {
            WebElement popupButton = explicitWaitForElement(closeLocatorPopUp, Duration.ofSeconds(5));
            waitUntilVisibleAndClickWithScroll(popupButton);
            log.info("My Cart page popup closed");
        } catch (TimeoutException ignored) {
            log.info("No popup appeared");
        }
    }

    private String getProductNameInCart() {
        return explicitWaitForElement(itemNameInCart).getText().trim();
    }

    private String getProductPriceInCart() {
        return explicitWaitForElement(itemPriceInCart).getText().trim();
    }


    public Map<ProductField, Object> getProductDetails() {
        Map<ProductField, Object> details = new HashMap<>();
        details.put(ProductField.NAME, getProductNameInCart());
        details.put(ProductField.PRICE, getProductPriceInCart());
        log.info("Availability check is not necessary, do quantity check instead");
        return details;
    }

    public String getProductQuantityInCart() { return explicitWaitForElement(itemQuantityInCart).getAttribute("value").trim();}

    private String getRemovedItemMessage(){return explicitWaitForElement(removedItemMessage).getText().trim();}


    private void clickIncreaseQuantityButton() {
        waitThenClickElement(increaseItemQuantityButton,5);
    }
    private void clickDecreaseQuantityButton() {
        WebElement button = explicitWaitForElement(decreaseItemQuantityButton);
        if (!button.isEnabled()) {
            log.info("Cannot decrease quantity. Only 1 item left and button is disabled");
            return;
        }
        clickElement(button);
    }

    private String getCartTotalPrice() {
        return explicitWaitForElement(cartTotalPrice).getAttribute("title").trim();
    }

    private String getProductTotal() {
        return explicitWaitForElement(productTotalPrice).getText().trim();
    }

    private String getShippingTotal() {
        try {
            return explicitWaitForElement(shipmentTotalPrice, Duration.ofSeconds(5)).getText().trim();
        } catch (TimeoutException e) {
            log.info("Shipping is free");
            return "0";
        }
    }

    private double parsePrice(String priceText) {
        return Double.parseDouble(priceText.replace("TL", "").trim().replace(".", "").replace(",", "."));
    }

    public void verifyAllPricesMatchesInCart() {
        Retry.run(() -> {
            double cartItemPrice = parsePrice(getProductPriceInCart());
            double productTotal = parsePrice(getProductTotal());
            double cartTotal = parsePrice(getCartTotalPrice());
            double shippingTotal = parsePrice(getShippingTotal());

            log.info("Comparing unit price of the product with the product total");
            if (Double.compare(cartItemPrice, productTotal) != 0) {
                throw new AssertionError("The unit price of the product does not match the 'Product Total'!\n"
                        + "Item(s) Total Price: " + cartItemPrice + "\n"
                        + "Cart Total: " + productTotal);
            }

            log.info("Checking whether cart total equals product total or product total plus shipping fee");
            if (Double.compare(productTotal, cartTotal) == 0) {
                log.info("Cart total matches product total (no shipping fee applied)");
                log.info("Cart Item Total:      {} TL", cartItemPrice);
                log.info("Product Total:        {} TL", productTotal);
                log.info("Shipping Total:       {} TL", shippingTotal);
                log.info("Final Cart Total:     {} TL", cartTotal);
            } else if (Double.compare(productTotal + shippingTotal, cartTotal) == 0) {
                log.info("Cart total matches product total + shipping");
                log.info("Cart Item Total:      {} TL", cartItemPrice);
                log.info("Product Total:        {} TL", productTotal);
                log.info("Shipping Total:       {} TL", shippingTotal);
                log.info("Final Cart Total:     {} TL", cartTotal);

            } else {
                throw new AssertionError("Cart total does not match product total or product+shipping total" +
                        "\nProduct Total: " + productTotal +
                        "\nShipping Total: " + shippingTotal +
                        "\nCart Total: " + cartTotal);
            }
        }, 5, 1000);
    }

    public void increaseProductQuantityBy(int increaseAmount) {
        int initialQuantity = Integer.parseInt(getProductQuantityInCart());
        int expectedQuantity = initialQuantity + increaseAmount;

        for (int i = 1; i <= increaseAmount; i++) {
            int currentExpected = initialQuantity + i;

            clickIncreaseQuantityButton();
            waitUntilValueEquals(itemQuantityInCart, String.valueOf(currentExpected), Duration.ofSeconds(5));
        }

        int actualQuantity = Integer.parseInt(getProductQuantityInCart());

        if (actualQuantity != expectedQuantity) {
            throw new AssertionError("Quantity mismatch after increasing!\n" +
                    "Expected: " + expectedQuantity + "\n" +
                    "Actual:   " + actualQuantity);
        }
        log.info("Product quantity increased successfully to {} ", actualQuantity);
    }

    public void decreaseProductQuantityBy(int decreaseAmount) {
        int initialQuantity = Integer.parseInt(getProductQuantityInCart());

        if (decreaseAmount > initialQuantity) {
            throw new IllegalArgumentException("Cannot decrease by more than current quantity.\n" +
                    "Current: " + initialQuantity + ", Requested: " + decreaseAmount);
        }

        int expectedQuantity = initialQuantity - decreaseAmount;

        for (int i = 1; i <= decreaseAmount; i++) {
            int currentExpected = initialQuantity - i;

            clickDecreaseQuantityButton();
            waitUntilValueEquals(itemQuantityInCart, String.valueOf(currentExpected), Duration.ofSeconds(5));

        }

        int actualQuantity = Integer.parseInt(getProductQuantityInCart());

        if (actualQuantity != expectedQuantity) {
            throw new AssertionError("Quantity mismatch after decreasing!\n" +
                    "Expected: " + expectedQuantity + "\n" +
                    "Actual:   " + actualQuantity);
        }
        log.info("Product quantity decreased successfully to: {} ",actualQuantity);
    }

    public void clickDeleteButton() {
        waitThenClickElement(deleteItemButton,5);
        log.info("Product removed from cart");
    }

    public void verifyProductRemovedMessageVisible() {
        if (!getRemovedItemMessage().contains("ürünü sepetinden kaldırıldı")) {
            throw new AssertionError("Removal message not found or does not match. Actual: " + getRemovedItemMessage());
        }
        log.info("Product removal message verified {} ",getRemovedItemMessage());
    }

}
