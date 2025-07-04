package framework.core;

import framework.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {


    public WebDriver createDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless"));
        boolean isRemote = Boolean.parseBoolean(ConfigReader.get("remote"));

        try {
            if (isRemote) {
                return createRemoteDriver(browser, headless);
            } else {
                return createLocalDriver(browser, headless);
            }


        } catch (MalformedURLException e) {
            throw new RuntimeException("Grid URL is invalid: " + e.getMessage(), e);
        }
    }

    private WebDriver createRemoteDriver(String browser, boolean headless) throws MalformedURLException {
        URL gridUrl = new URL(ConfigReader.get("grid.url"));

        switch (browser) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("--headless");
                return new RemoteWebDriver(gridUrl, firefoxOptions);

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                return new RemoteWebDriver(gridUrl, edgeOptions);

            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_setting_values.notifications", 2);
                chromeOptions.setExperimentalOption("prefs", prefs);
                if (headless) {
                    chromeOptions.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                    } else {
                        chromeOptions.addArguments("--start-maximized",
                                "--no-sandbox",
                                "--disable-dev-shm-usage");
                    }
                    return new RemoteWebDriver(gridUrl, chromeOptions);
                }
        }


        private WebDriver createLocalDriver (String browser,boolean headless){
            switch (browser) {
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (headless) firefoxOptions.addArguments("--headless");
                    return new org.openqa.selenium.firefox.FirefoxDriver(firefoxOptions);

                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    return new org.openqa.selenium.edge.EdgeDriver(edgeOptions);

                case "chrome":
                default:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    Map<String, Object> prefs = new HashMap<>();
                    prefs.put("profile.default_content_setting_values.notifications", 2);
                    chromeOptions.setExperimentalOption("prefs", prefs);
                    if (headless) {
                        chromeOptions.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                    } else {
                        chromeOptions.addArguments("--start-maximized");
                    }
                    return new ChromeDriver(chromeOptions);
            }
        }
    }

