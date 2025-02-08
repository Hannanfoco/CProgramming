package UserRegistrationAndLogin;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRegistrationSameEmail {

    private static WebDriver webDriver;
    private static String baseUrl;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "/Users/macbook/Downloads/chromedriver-mac-arm64 2/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);

        // Maximize the browser window
        webDriver.manage().window().maximize();

        baseUrl = "https://www.ekupi.ba";
    }

    @Test
    public void testUserRegistration() {
        System.out.println("Opening the homepage...");
        webDriver.get(baseUrl);

        System.out.println("Navigating to the registration page...");
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        WebElement registrationLink = wait.until(ExpectedConditions.elementToBeClickable(By.className("login_link")));
        registrationLink.click();

        System.out.println("Waiting for the registration form...");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ekupiRegisterForm")));

        System.out.println("Filling out the form...");
        fillField(wait, "register.firstName", "mala");
        fillField(wait, "register.lastName", "maca");
        fillField(wait, "register.email", "mala@maca.com");
        fillField(wait, "password", "Test@1234");
        fillField(wait, "register.checkPwd", "Test@1234");

        System.out.println("Checking the checkboxes...");
        safeClick(wait, By.xpath("//input[@name='allowInfo']"));
        safeClick(wait, By.id("registerChkTermsConditions"));
        safeClick(wait, By.xpath("//input[@name='gdprCheck']"));

        System.out.println("Submitting the form...");
        safeClick(wait, By.cssSelector("#ekupiRegisterForm button[type='submit']"));

        System.out.println("Waiting for the error message...");
        WebElement errorElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email.errors")));
        String actualText = errorElement.getText();
        System.out.println("Actual error message text: \"" + actualText + "\"");
        String expectedText = "Profil sa navedenom email adresom već postoji.";

        // Normalize text before comparison
        actualText = actualText.replaceAll("\\s+", " ").trim();
        expectedText = expectedText.replaceAll("\\s+", " ").trim();

        assertEquals(expectedText, actualText, "The error message text is incorrect.");

        System.out.println("Test passed! The error message is displayed correctly.");
    }

    private void fillField(WebDriverWait wait, String fieldId, String value) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(By.id(fieldId)));
        field.clear();
        field.sendKeys(value);
        try {
            Thread.sleep(500); // Small delay to mimic real user behavior
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void safeClick(WebDriverWait wait, By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollToElement(element);
        try {
            element.click();
        } catch (Exception e) {
            System.out.println("Element click failed, trying JavaScript click as fallback...");
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", element);
        }
    }

    private void scrollToElement(WebElement element) {
        String scrollScript = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";
        ((JavascriptExecutor) webDriver).executeScript(scrollScript, element);
    }

    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}