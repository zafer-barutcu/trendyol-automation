package framework.utils;

import org.intellij.lang.annotations.Language;
import org.openqa.selenium.By;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Collection of custom {@link By} implementations that can be used with selenium
 */

public final class CustomBy {

    private CustomBy(){}

    public static By xpathOr(@Language("XPath") String... xpaths) {
        return By.xpath(Arrays.stream(xpaths).collect(Collectors.joining(" | ", "(", ")")));
    }

    public static By searchResultPrice() {
        return xpathOr(".//div[contains(@class, 'price-item discounted')]",
                ".//div[contains(@class, 'price-item lowest-price-discounted')]",
                ".//div[contains(@class, 'price-item')]");

    }

}
