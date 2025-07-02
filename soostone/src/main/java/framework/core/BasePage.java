package framework.core;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * BasePage provides the core WebDriver instance and initializes web elements
 * using PageFactory (if web element initialization is required).
 * All page objects should extend this class.
 */
public class BasePage {
    protected WebDriver driver;

    public BasePage() {
        this.driver = ThreadLocalDriver.getDriver();
        PageFactory.initElements(driver, this);
    }
}