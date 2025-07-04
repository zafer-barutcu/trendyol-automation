package framework.pages;

import framework.context.BaseTextContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import static framework.utils.CustomBy.searchResultPrice;

public class SearchResultsPage extends BaseTextContext {

    private static final Logger log = LoggerFactory.getLogger(ProductDetailPage.class);
    @FindBy(css = "div.p-card-wrppr.with-campaign-view.add-to-bs-card")
    protected List<WebElement> productCards;
    private final By addToCartBtnFromSearchResult = By.cssSelector("div.add-to-bs-tx");


    public boolean areSearchResultsDisplayed() {
        return productCards.size() > 0;
    }

    public boolean atLeastOneResultContainsSearchKeyword(String phrase) {
        int matchCount = 0;
        phrase = phrase.toLowerCase();

        for (WebElement card : productCards) {
            String title = card.getAttribute("title").toLowerCase();
            if (title.contains(phrase)) {
                matchCount++;
            }
        }
        log.info("{} result(s) contain the keyword {} ",matchCount,phrase);
        return matchCount > 0;
    }


    private String selectedProductName;
    private String selectedProductPrice;
    private boolean isSelectedProductAvailable;

    public void storeAndClickFirstMatchingProduct(String keyword) {
        for (WebElement card : productCards) {
            String title = card.getAttribute("title");
            if (title != null && title.toLowerCase().contains(keyword.toLowerCase())) {

                selectedProductName = title;

                WebElement priceElement = card.findElement(searchResultPrice());
                selectedProductPrice = priceElement.getText().trim();

                try {
                    WebElement addToCartButton = card.findElement(addToCartBtnFromSearchResult);
                    isSelectedProductAvailable = addToCartButton.isEnabled();
                } catch (NoSuchElementException e) {
                    isSelectedProductAvailable = false;
                    log.warn("Add to Cart button not found for product: {}", selectedProductName);
                }

                log.info("Selected product from search results:");
                log.info(" - Name: {}", selectedProductName);
                log.info(" - Price: {}", selectedProductPrice);
                log.info(" - Available: {}", isSelectedProductAvailable);

                log.info("Clicking on the matching product card...");
                waitUntilVisibleAndClickWithScroll(card);
                return;
            }
        }
        throw new RuntimeException("No matching product found with keyword: " + keyword);
    }

    public Map<ProductField, Object> getSelectedProductDetails() {
        return getProductDetails(selectedProductName, selectedProductPrice, isSelectedProductAvailable);
    }
}
