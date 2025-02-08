package cookieandstorage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalStorageTest {
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

        // Set timeouts
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60)); // Increased timeout
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(20)); // Explicit wait

        baseUrl = "https://www.ekupi.ba";

        // Perform login as part of setup
        try {
            webDriver.get(baseUrl);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

            WebElement loginRegisterButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Prijava / Registracija')]")));
            loginRegisterButton.click();

            wait.until(ExpectedConditions.urlContains("/login"));

            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("j_username")));
            emailField.sendKeys("maajumaju@Nesto.com");

            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("j_password")));
            passwordField.sendKeys("Mujo@1234");

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
    public void testLocalStorage() {
        try {
            webDriver.get(baseUrl);

            JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;

            // Check Local Storage for 'jwt_token'
            String jwtToken = (String) jsExecutor.executeScript("return window.localStorage.getItem('jwt_token');");
            assertNull(jwtToken, "JWT Token found in Local Storage when it should not be!");
            System.out.println("JWT Token from Local Storage (should be null): " + jwtToken);

            // Check Cookies for 'auth_token'
            Cookie authCookie = webDriver.manage().getCookieNamed("auth_token");
            if (authCookie != null) {
                System.out.println("JWT Token from Cookie: " + authCookie.getValue());
            } else {
                System.out.println("No auth_token cookie found.");
            }

            // Check Session Storage for 'jwt_token'
            String jwtTokenFromSessionStorage = (String) jsExecutor.executeScript("return window.sessionStorage.getItem('jwt_token');");
            System.out.println("JWT Token from Session Storage: " + jwtTokenFromSessionStorage);

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
