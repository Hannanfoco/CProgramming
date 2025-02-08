package Navigation;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DropdownTest {
    private static WebDriver webDriver;
    private static String baseUrl;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "/Users/macbook/Downloads/chromedriver-mac-arm64 2/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        baseUrl = "https://www.ekupi.ba";
    }

    @Test
    public void testKucanskiAparatiDropdown() {
        webDriver.get(baseUrl);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        Actions actions = new Actions(webDriver);


        WebElement kucanskiAparati = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Kućanski aparati")));
        actions.moveToElement(kucanskiAparati).perform();

        List<WebElement> dropdownLinks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".dropdown-menu a")));


        for (WebElement link : dropdownLinks) {
            String linkText = link.getText();


            link.click();


            WebElement pageTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));
            String pageTitleText = pageTitle.getText();


            assertTrue(pageTitleText.contains(linkText), "Page title does not match dropdown link: " + linkText);


            webDriver.navigate().back();


            kucanskiAparati = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Kućanski aparati")));
            actions.moveToElement(kucanskiAparati).perform();
            dropdownLinks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".dropdown-menu a")));
        }
    }




    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
