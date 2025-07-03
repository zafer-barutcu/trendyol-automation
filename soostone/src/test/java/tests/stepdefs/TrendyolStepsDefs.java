package tests.stepdefs;

import framework.context.BaseTextContext;
import framework.pages.HomePage;
import framework.pages.MyCartPage;
import framework.pages.ProductDetailPage;
import framework.pages.SearchResultsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Map;

import static framework.context.BaseTextContext.ProductField.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrendyolStepsDefs {

    private static final Logger log = LoggerFactory.getLogger(TrendyolStepsDefs.class);
    HomePage homePage = new HomePage();
    SearchResultsPage searchResultsPage = new SearchResultsPage();
    ProductDetailPage productDetailPage = new ProductDetailPage();
    MyCartPage myCartPage = new MyCartPage();
    Map<BaseTextContext.ProductField, Object> expected;
    Map<BaseTextContext.ProductField, Object> actual;


    @Given("I search {string}")
    public void iSearch(String key)  {
        log.info("Searching for keyword: {}", key);
        homePage.prepareHomePage();
        homePage.searchProduct(key);
    }

    @Then("Verify search results displayed")
    public void verifySearchResultsDisplayed() {
        log.info("Verifying if search results are displayed");
        assertTrue(searchResultsPage.areSearchResultsDisplayed());
    }

    @Then("Verify search results contain {string}")
    public void verifySearchResultsContain(String keyword) {
        log.info("Checking if at least one result contains product full keyword: {}", keyword);
        assertTrue(searchResultsPage.atLeastOneResultContainsSearchKeyword(keyword));
    }

    @When("I click {string} to open its details page")
    public void iClickToOpenItsDetailsPage(String keyword) {
        log.info("Clicking on product to open detail page: {}", keyword);
        searchResultsPage.storeAndClickFirstMatchingProduct(keyword);
    }

    @Then("Verify product name, price, and availability status are displayed correctly")
    public void verifyProductNamePriceAndAvailabilityStatusAreDisplayedCorrectly() {
        log.info("Verifying product name, price, and availability on product detail page");
        productDetailPage.dismissLocationPopupIfVisible();
        expected = searchResultsPage.getSelectedProductDetails();
        actual = productDetailPage.getProductDetails();

        assertTrue("Name mismatch", ((String) actual.get(NAME)).toLowerCase()
                .contains(((String) expected.get(NAME)).toLowerCase()));

        assertEquals("Price mismatch", expected.get(PRICE), actual.get(PRICE));
        assertEquals("Availability mismatch", expected.get(AVAILABILITY), actual.get(AVAILABILITY));

        log.info("Product details matched successfully");
    }

    @When("I add the selected product to the cart")
    public void iAddTheSelectedProductToTheCart() {
        log.info("Adding selected product to cart");
        expected = productDetailPage.getProductDetails();
        productDetailPage.addToCartFromDetailPage();
        productDetailPage.verifyProductAddedToCart();
        productDetailPage.goToCartPage();
        log.info("Navigated to cart page");
        assertTrue("Failed to navigate to Cart page", myCartPage.isAtCartPage());
    }

    @Then("Verify the product appears in the cart with the correct details")
    public void verifyTheProductAppearsInTheCartWithTheCorrectDetails() {
        log.info("Verifying product in cart matches expected details");
        myCartPage.checkAndDismissIfLocatorPopupInCartPageIsVisible();
        actual = myCartPage.getProductDetails();
        assertTrue("Product name mismatch!",
                ((String) actual.get(NAME)).replaceAll("\\s+","").toLowerCase()
                        .contains(((String) expected.get(NAME)).replaceAll("\\s+", "").toLowerCase()));

        assertEquals("Price mismatch", expected.get(PRICE), actual.get(PRICE));

        assertEquals("Quantity mismatch", "1", myCartPage.getProductQuantityInCart());

        log.info("Product details in cart matched expected values");
    }

    @Then("Verify the cart total is accurate")
    public void verifyTheCartTotalIsAccurate() {
        log.info("Verifying cart total price including product and shipping calculations");
        myCartPage.verifyAllPricesMatchesInCart();
    }

    @When("I add {int} more product to the cart")
    public void iAddMoreProductsToTheCart(int quantityToAdd) {
        log.info("Increasing product quantity in cart by {}", quantityToAdd);
        myCartPage.increaseProductQuantityBy(quantityToAdd);
    }

    @When("I remove {int} product from the cart")
    public void iRemoveTheProductFromTheCart(int quantityToRemove) {
        log.info("Decreasing product quantity in cart by {}", quantityToRemove);
        myCartPage.decreaseProductQuantityBy(quantityToRemove);
    }

    @When("I remove the item from the cart")
    public void iRemoveTheItemFromTheCart() {
        log.info("Removing the product from cart");
        myCartPage.clickDeleteButton();
    }

    @Then("Verify the product is no longer listed in the cart")
    public void verifyTheProductIsNoLongerListedInTheCart() {
        log.info("Verifying the product has been removed from the cart");
        myCartPage.verifyProductRemovedMessageVisible();
    }

}