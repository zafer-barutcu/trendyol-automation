package framework.pages;

import framework.context.BaseTextContext;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends BaseTextContext {

    private static final Logger log = LoggerFactory.getLogger(HomePage.class);

    private final By closeHomePagePopUp = By.xpath("//div[@class='modal-close' and @title='Kapat']");
    private final By acceptAllCookiesButton = By.xpath("//button[contains(text(),'Tümünü Kabul Et')]");

    public void searchProduct(String text){
        explicitWaitForElement(productSearchBox);
        sendKeysTo(productSearchBox, text);
        clickElement(productSearch); // or .sendKeys(Keys.ENTER)
    }

    private void closeHomePagePopupIfVisible() {
        try {
            waitThenClickElement(closeHomePagePopUp,30);
        } catch (TimeoutException ignored) {
            log.info("Home page pop up is not present or cookie banner removed first");
        }
    }

    private void acceptCookiesIfPresent() {
        try {
            waitThenClickElement(acceptAllCookiesButton,30);
        } catch (Exception ignored) {
            log.info("Cookie banner not present");
        }
    }

    public void prepareHomePage() {
        acceptCookiesIfPresent();
        closeHomePagePopupIfVisible();
    }






}