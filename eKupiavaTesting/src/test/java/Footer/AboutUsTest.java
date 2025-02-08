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

import static org.antlr.tool.ErrorManager.assertTrue;

public class AboutUsTest {
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
    public void testAboutUsSectionPresence() {
        webDriver.get(baseUrl);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement aboutUsSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='O nama']")));

        assertTrue(aboutUsSection.isDisplayed(), "'O nama' section is not displayed!");
    }

    @Test
    public void testSubheadersNavigationAndContent() throws InterruptedException {

        webDriver.get(baseUrl);

        String[] subheaders = {
                "Vizija i misija",
                "Prednosti kupovine na eKupi",
                "Gdje poslujemo",
                "Ko je Kupko?",
                "Posao na eKupi",
                "Dobijene nagrade"
        };

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));

        for (String subheaderText : subheaders) {
            try {

                WebElement subheaderLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(subheaderText)));
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", subheaderLink);
                Thread.sleep(500);


                wait.until(ExpectedConditions.elementToBeClickable(subheaderLink)).click();

                wait.until(ExpectedConditions.urlContains(getExpectedUrlFragment(subheaderText)));


                WebElement body = webDriver.findElement(By.tagName("body"));
                assertTrue(body.getText().contains(getExpectedContent(subheaderText)), "Content mismatch for " + subheaderText);


                webDriver.navigate().back();
                Thread.sleep(500);
            } catch (Exception e) {
                System.err.println("Failed for subheader: " + subheaderText + " with error: " + e.getMessage());
            }
        }
    }


    private String getExpectedUrlFragment(String subheaderText) {
        switch (subheaderText) {
            case "Vizija i misija":
                return "/vizija-i-misija";
            case "Prednosti kupovine na eKupi":
                return "/prednosti-kupovine";
            case "Gdje poslujemo":
                return "/gdje-poslujemo";
            case "Ko je Kupko?":
                return "/ko-je-kupko";
            case "Posao na eKupi":
                return "/posao-na-ekupi";
            case "Dobijene nagrade":
                return "/dobijene-nagrade";
            default:
                return "";
        }
    }

    private String getExpectedContent(String subheaderText) {
        switch (subheaderText) {
            case "Vizija i misija":
                return "Vizija";
            case "Prednosti kupovine na eKupi":
                return "Prednosti kupovine";
            case "Gdje poslujemo":
                return "Poslujemo";
            case "Ko je Kupko?":
                return "Kupko";
            case "Posao na eKupi":
                return "Karijere";
            case "Dobijene nagrade":
                return "Nagrade";
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