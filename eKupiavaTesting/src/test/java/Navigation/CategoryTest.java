package Navigation;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryTest {
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
    public void testKucanskiAparatiCategory() {

        webDriver.get(baseUrl);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement kucanskiAparatiLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='Kućanski aparati']")));
        String categoryName = kucanskiAparatiLink.getText().trim();
        kucanskiAparatiLink.click();
        WebElement pageTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));
        String pageTitleText = pageTitle.getText().trim();


        System.out.println("Expected Title: " + categoryName);
        System.out.println("Actual Title: " + pageTitleText);


        assertTrue(pageTitleText.equalsIgnoreCase(categoryName),
                "Page title does not match the clicked category. Expected: " + categoryName + ", but found: " + pageTitleText);
    }


    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}

