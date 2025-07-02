package tests.hooks;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import framework.core.DriverFactory;
import framework.core.ThreadLocalDriver;
import framework.pages.ProductDetailPage;
import framework.utils.ConfigReader;
import framework.utils.ExtentManager;
import framework.utils.ExtentTestManager;
import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);
    private static final ExtentReports extent = ExtentManager.getInstance();

    @Before
    public void setUp(Scenario scenario) {
        log.info("Starting scenario: {}", scenario.getName());
        log.info("Initializing WebDriver and navigating to: {}", ConfigReader.get("baseUrl"));
        WebDriver driver = new DriverFactory().createDriver();
        ThreadLocalDriver.setDriver(driver);
        driver.get(ConfigReader.get("baseUrl"));
        ExtentTest test = extent.createTest(scenario.getName());
        ExtentTestManager.setTest(test);
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed()) {
            log.warn("Step failed: capturing screenshot...");
            try {
                String screenshot = ((TakesScreenshot) ThreadLocalDriver.getDriver())
                        .getScreenshotAs(OutputType.BASE64);

                ExtentTestManager.getTest().fail(
                        "Step failed",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot, "Failure Screenshot").build()
                );
            } catch (Exception e) {
                log.error("Failed to capture screenshot: {}", e.getMessage());
                ExtentTestManager.getTest().fail("Screenshot capture failed: " + e.getMessage());
            }
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        log.info("Scenario finished: {}", scenario.getName());
        if (scenario.isFailed()) {
            log.error("Scenario result: FAILED");
            ExtentTestManager.getTest().fail("Scenario failed");
        } else {
            log.info("Scenario result: PASSED");
            ExtentTestManager.getTest().pass("Scenario passed");
        }

        ThreadLocalDriver.getDriver().quit();
        ThreadLocalDriver.unload();
        extent.flush();
        ExtentTestManager.removeTest();
    }
}
