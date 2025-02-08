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

public class HelpFooterTest {
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
    public void testHelpSectionLinks() {
        webDriver.get(baseUrl);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        String[] links = {"Kako se registrovati", "Kako kupovati na eKupi", "Narudžbe telefonom ili e-mailom", "Kako iskoristiti poklon KOD", "Ponuda za firme", "Česta pitanja"};

        for (String link : links) {
            try {
                WebElement sectionLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(link)));
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", sectionLink);
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
            case "Kako se registrovati":
                return "/kako-se-registrovati";
            case "Kako kupovati na eKupi":
                return "/kako-kupovati";
            case "Narudžbe telefonom ili e-mailom":
                return "/narudzbe-telefonom";
            case "Kako iskoristiti poklon KOD":
                return "/poklon-kod";
            case "Ponuda za firme":
                return "/ponuda-za-firme";
            case "Česta pitanja":
                return "/faq";
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

