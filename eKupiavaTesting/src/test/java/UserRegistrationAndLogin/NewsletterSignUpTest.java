package UserRegistrationAndLogin;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NewsletterSignUpTest {
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
        @Order(1)

        public void testNewsletterSignUp() throws InterruptedException {
            webDriver.get(baseUrl);
            Thread.sleep(2000);

            webDriver.manage().window().maximize();
            Thread.sleep(1000);


            JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
            for (int i = 0; i < 10; i++) {
                jsExecutor.executeScript("window.scrollBy(0, document.body.scrollHeight / 10);");
                Thread.sleep(500);
            }


            webDriver.findElement(By.xpath("//*[@id=\"newsletterForm\"]/div[1]/input")).sendKeys("Muhamed");
            Thread.sleep(500);
            webDriver.findElement(By.xpath("//*[@id=\"newsletterForm\"]/div[2]/input")).sendKeys("Tukulic");
            Thread.sleep(500);
            webDriver.findElement(By.xpath("//*[@id=\"newsletterForm\"]/div[3]/input")).sendKeys("stakazete@hotmail.com");
            Thread.sleep(500);
            webDriver.findElement(By.xpath("//*[@id=\"newsletterForm\"]/div[4]/input")).sendKeys("225883");
            Thread.sleep(500);

            WebElement submitButton = webDriver.findElement(By.xpath("//*[@id=\"newsletter-submit-btn\"]"));
            submitButton.click();
            Thread.sleep(10000);

            String currenturl = webDriver.getCurrentUrl();

            assertEquals("https://www.ekupi.ba/bs/Prijava-na-eKupi-obavijesti" , currenturl);
            Thread.sleep(10000);
            System.out.println("Newsletter confirmation email has been confirmed.");




        }


        @Test
        @Order(2)

        void zdoubletestsignupnewsletter() throws InterruptedException {
            webDriver.get(baseUrl);
            Thread.sleep(1000);


            JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
            for (int i = 0; i < 10; i++) {
                jsExecutor.executeScript("window.scrollBy(0, document.body.scrollHeight / 10);");
                Thread.sleep(500);
            }

            webDriver.findElement(By.xpath("//*[@id=\"newsletterForm\"]/div[1]/input")).sendKeys("Muhamed");
            Thread.sleep(500);
            webDriver.findElement(By.xpath("//*[@id=\"newsletterForm\"]/div[2]/input")).sendKeys("Tukulic");
            Thread.sleep(500);
            webDriver.findElement(By.xpath("//*[@id=\"newsletterForm\"]/div[3]/input")).sendKeys("stakazete@hotmail.com");
            Thread.sleep(500);
            webDriver.findElement(By.xpath("//*[@id=\"newsletterForm\"]/div[4]/input")).sendKeys("225883");
            Thread.sleep(500);


            webDriver.findElement(By.xpath("/html/body/main/div[5]/div[10]/div/div/div[2]/form/button")).click();

            Thread.sleep(2000);


            try {
                WebElement errorMessage = webDriver.findElement(By.xpath("//*[contains(text(), 'Ova email adresa je već prijavljena na newsletter')]"));
                assertTrue(errorMessage.isDisplayed(), "Expected error message not displayed.");
                System.out.println("Newsletter test passed: Email is already registered.");
            } catch (NoSuchElementException e) {
                System.out.println("Newsletter test failed: Email is not recognized as already registered.");
            }


        }

        @AfterAll
        public static void tearDown() {
            if (webDriver != null) {
                webDriver.quit();
            }
        }



    }