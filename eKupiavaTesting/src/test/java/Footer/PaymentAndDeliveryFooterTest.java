package Footer;


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

public class PaymentAndDeliveryFooterTest {
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
    public void testPaymentAndDeliveryLinks() {
        webDriver.get(baseUrl);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        String[] links = {
                "Načini plaćanja",
                "Sigurnost plaćanja",
                "Lično preuzimanje (eKupi&poKupi)",
                "Brza i pouzdana dostava",
                "DUS - Dostava u stan",
                "Prikup starog uređaja"
        };

        for (String link : links) {
            try {
                WebElement sectionLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(link)));
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", sectionLink);
                Thread.sleep(1000); // Ensure scrolling is visible
                sectionLink.click();
                assertTrue(webDriver.getCurrentUrl().contains(getUrlFragment(link)), "Failed to navigate to: " + link);
                webDriver.navigate().back();
            } catch (Exception e) {
                System.err.println("Error while testing link: " + link + " - " + e.getMessage());
            }
        }
    }

    private String getUrlFragment(String linkText) {
        switch (linkText) {
            case "Načini plaćanja":
                return "/nacini-placanja";
            case "Sigurnost plaćanja":
                return "/sigurnost-placanja";
            case "Lično preuzimanje (eKupi&poKupi)":
                return "/licno-preuzimanje";
            case "Brza i pouzdana dostava":
                return "/brza-dostava";
            case "DUS - Dostava u stan":
                return "/dus-dostava-u-stan";
            case "Prikup starog uređaja":
                return "/prikup-starog-uredjaja";
            default:
                return "";
        }
    }

    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
