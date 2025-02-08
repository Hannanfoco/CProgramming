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



public class Login {
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
    public void testNavigateAndLogin() {

        webDriver.get(baseUrl);


        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement loginRegisterButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Prijava / Registracija')]")));
        loginRegisterButton.click();


        wait.until(ExpectedConditions.urlContains("/login"));


        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("j_username")));
        emailField.sendKeys("testuser@example.com");


        WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("j_password")));
        passwordField.sendKeys("testpassword");


        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
        loginButton.click();


    }







    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
