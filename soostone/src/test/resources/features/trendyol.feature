@productsearch
Feature: Trendyol website - product search, add to cart, and remove from cart

  Scenario: Search for a product and perform full cart operations
    Given I search "kablosuz kulaklık"
    Then  Verify search results displayed
    Then  Verify search results contain "kablosuz kulaklık"
    When  I click "kablosuz kulaklık" to open its details page
    Then  Verify product name, price, and availability status are displayed correctly
    When  I add the selected product to the cart
    Then  Verify the product appears in the cart with the correct details
    And   Verify the cart total is accurate
    When  I add 1 more product to the cart
    Then  Verify the cart total is accurate
    When  I remove 1 product from the cart
    Then  Verify the cart total is accurate
    When  I remove the item from the cart
    Then  Verify the product is no longer listed in the cart


