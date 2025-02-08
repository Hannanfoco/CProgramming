package UserRegistrationAndLogin;

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

import static org.antlr.tool.ErrorManager.assertTrue;


public class PasswordRecovery {
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
    public void testPasswordRecovery() {
        webDriver.get(baseUrl);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));

        WebElement loginRegisterButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'Prijava / Registracija')]")
        ));
        loginRegisterButton.click();

        wait.until(ExpectedConditions.urlContains("/login"));

        WebElement forgotPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a.js-password-forgotten")
        ));
        forgotPasswordLink.click();

        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("forgottenPwd.email")));
        emailField.sendKeys("testuser@example.com");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("forgotten-password-btn")));
        submitButton.click();

        System.out.println("Waiting for the confirmation message...");
        WebElement confirmationMessageElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("#validEmail")
        ));

        String actualText = confirmationMessageElement.getText();
        String expectedText = "Poslali smo Vam email s linkom za promjenu lozinke";

        System.out.println("Actual confirmation message: \"" + actualText + "\"");
        System.out.println("Expected confirmation message: \"" + expectedText + "\"");

        assertTrue(actualText.trim().contains(expectedText),
                "The confirmation message text does not match the expected text.");

        System.out.println("Test passed! The confirmation message is displayed correctly.");
    }




    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

}
