package cookieandstorage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtTest {
    private static WebDriver webDriver;
    private static WebDriverWait wait;
    private static String baseUrl;

    @BeforeAll
    public static void setUp() {
        // Set the path to ChromeDriver and initialize WebDriver
        System.setProperty("webdriver.chrome.driver", "/Users/macbook/Downloads/chromedriver-mac-arm64 2/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();

        // Set explicit wait
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));

        baseUrl = "https://www.ekupi.ba";

        try {
            // Navigate to the base URL
            webDriver.get(baseUrl);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

            // Click the login/register button
            WebElement loginRegisterButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Prijava / Registracija')]")));
            loginRegisterButton.click();

            // Wait for the login page to load
            wait.until(ExpectedConditions.urlContains("/login"));

            // Enter login credentials
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("j_username")));
            emailField.sendKeys("maajumaju@Nesto.com");

            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("j_password")));
            passwordField.sendKeys("Mujo@1234");

            // Click the login button
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
            loginButton.click();

            // Verify successful login
            WebElement mojProfilLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//a[@href='https://www.ekupi.ba/bs/my-account/orders' and text()='Moj profil']"))
            );
            assertTrue(mojProfilLink.isDisplayed(), "The 'Moj profil' link is not displayed on the page!");

            System.out.println("Login successful. Ready to proceed with tests.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to log in during setup.", e);
        }
    }

    @Test
    public void testJwtAndCookies() {
        try {
            // Navigate to the base URL
            webDriver.get(baseUrl);

            // Retrieve all cookies
            Set<Cookie> cookies = webDriver.manage().getCookies();
            boolean jwtFound = false;

            // Print and check for JWT cookie
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
                if (cookie.getName().equalsIgnoreCase("jwt")) {
                    jwtFound = true;
                    System.out.println("JWT Token Found: " + cookie.getValue());
                }
            }

            // Assert that JWT token is not found
            assertFalse(jwtFound, "JWT token should not be found in cookies!");

            // Verify the URL starts with HTTPS
            String currentUrl = webDriver.getCurrentUrl();
            System.out.println("Current URL: " + currentUrl);
            assertTrue(currentUrl.startsWith("https://"), "The current URL does not use the HTTPS protocol!");
        } catch (Exception e) {
            throw new RuntimeException("Error during JWT and cookies test.", e);
        }
    }

    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
