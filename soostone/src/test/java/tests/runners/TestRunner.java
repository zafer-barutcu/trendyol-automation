package tests.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"tests.stepdefs", "tests.hooks"},
//  plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
    plugin = {"pretty",
            "html:reports/cucumber-html-report",
            "json:reports/cucumber.json",},
    monochrome = true
)
public class TestRunner {
}