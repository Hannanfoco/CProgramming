package filter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.antlr.tool.ErrorManager.assertTrue;

public class SerachTest {

    private static WebDriver webDriver;
    private static String baseUrl;

    @BeforeAll
    public static void setUp() {
        // Set the path to ChromeDriver and initialize WebDriver
        System.setProperty("webdriver.chrome.driver", "/Users/macbook/Downloads/chromedriver-mac-arm64 2/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();

        // Set an explicit timeout for page load
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));  // Increased timeout

        baseUrl = "https://www.ekupi.ba";
    }

    @Test
    public void testSearchDropdownContainsIphone() throws InterruptedException {
        // Navigate to the homepage
        webDriver.get("https://www.ekupi.ba");

        try {
            // Locate the search input field
            WebElement searchInput = webDriver.findElement(By.id("js-site-search-input"));

            // Clear the input field and type "iPhone" character by character
            searchInput.clear();
            String query = "iPhone";
            for (char c : query.toCharArray()) {
                searchInput.sendKeys(String.valueOf(c));
                Thread.sleep(300); // Delay between each character
            }

            // Wait for the dropdown to populate
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            List<WebElement> dropdownOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".ui-menu-item-wrapper")));

            // Check if any of the dropdown options contain the term "iPhone"
            boolean containsIphone = false;
            for (WebElement option : dropdownOptions) {
                if (option.getText().toLowerCase().contains("iphone")) {
                    containsIphone = true;
                    break;
                }
            }

            // Assert that "iPhone" is present in the dropdown options
            assertTrue(containsIphone, "The dropdown does not contain 'iPhone'.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testSearchDropdownContainsIphone256() throws InterruptedException {
        // Navigate to the homepage
        webDriver.get("https://www.ekupi.ba");

        try {
            // Locate the search input field
            WebElement searchInput = webDriver.findElement(By.id("js-site-search-input"));

            // Clear the input field and type "iPhone 256" character by character
            searchInput.clear();
            String query = "iPhone 256";
            for (char c : query.toCharArray()) {
                searchInput.sendKeys(String.valueOf(c));
                Thread.sleep(300); // Delay between each character
            }

            // Wait for the dropdown to populate
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            List<WebElement> dropdownOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".ui-menu-item-wrapper")));

            // Log all dropdown options for debugging
            System.out.println("Dropdown options:");
            for (WebElement option : dropdownOptions) {
                System.out.println(option.getText());
            }

            // Check if any of the dropdown options contain the term "iPhone 256"
            boolean containsIphone256 = false;
            for (WebElement option : dropdownOptions) {
                if (option.getText().toLowerCase().contains("iphone 256")) {
                    containsIphone256 = true;
                    break;
                }
            }

            // Assert that "iPhone 256" is present in the dropdown options
            assertTrue(containsIphone256, "The dropdown does not contain 'iPhone 256'.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchDropdownHandlesTypo() throws InterruptedException {
        // Navigate to the homepage
        webDriver.get("https://www.ekupi.ba");

        try {
            // Locate the search input field
            WebElement searchInput = webDriver.findElement(By.id("js-site-search-input"));

            // Clear the input field and type "iphoen" character by character
            searchInput.clear();
            String typoQuery = "iphoen";
            for (char c : typoQuery.toCharArray()) {
                searchInput.sendKeys(String.valueOf(c));
                Thread.sleep(300); // Delay between each character
            }

            // Wait for the dropdown to populate
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            List<WebElement> dropdownOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".ui-menu-item-wrapper")));

            // Log all dropdown options for debugging
            System.out.println("Dropdown options for 'iphoen':");
            for (WebElement option : dropdownOptions) {
                System.out.println(option.getText());
            }

            // Check if any of the dropdown options contain the term "iPhone"
            boolean containsIphone = false;
            for (WebElement option : dropdownOptions) {
                if (option.getText().toLowerCase().contains("iphone")) {
                    containsIphone = true;
                    break;
                }
            }

            // Assert that "iPhone" is present in the dropdown options
            assertTrue(containsIphone, "The dropdown does not contain 'iPhone' when searching with 'iphoen'.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }














    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
