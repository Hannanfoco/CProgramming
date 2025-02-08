package Cart;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddToCartWithLogin {
    private static WebDriver webDriver;
    private static String baseUrl;
    static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        // Set the path to ChromeDriver and initialize WebDriver
        System.setProperty("webdriver.chrome.driver", "/Users/macbook/Downloads/chromedriver-mac-arm64 2/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
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
    @Order(1)
    @DisplayName("Verify Login")
    public void testLoginVerification() {
        // This test just verifies the login status
        WebElement mojProfilLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='https://www.ekupi.ba/bs/my-account/orders' and text()='Moj profil']"))
        );
        assertTrue(mojProfilLink.isDisplayed(), "The 'Moj profil' link is not displayed on the page!");
    }

    @Test
    @Order(2)
    @DisplayName("Test Adding a Single Item to the Cart")
    public void testAddSingleItemToCart() throws InterruptedException {
        // Navigate to the homepage
        webDriver.get(baseUrl);

        // Navigate to "Alati i Mašine" and select "Aku Bušilice"
        WebElement alatimasine = webDriver.findElement(By.xpath("/html/body/main/header/nav[3]/div/ul[2]/li[7]/span[1]/a"));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(alatimasine).perform();
        Thread.sleep(2000);

        WebElement akubusilice = webDriver.findElement(By.xpath("/html/body/main/header/nav[3]/div/ul[2]/li[7]/div/div/div[1]/ul/li[2]/a"));
        akubusilice.click();
        Thread.sleep(2000);

        // Select the first product and add it to the cart
        WebElement firstProduct = webDriver.findElement(By.xpath("(//div[contains(@class, 'product-item')]//a)[1]"));
        firstProduct.click();
        Thread.sleep(2000);

        webDriver.findElement(By.xpath("/html/body/main/div[5]/div[2]/div[3]/div/div/div[2]/div/div[2]/div/div/div[2]/div[1]/form[2]/button")).click();
        Thread.sleep(1000);

        // Proceed to the cart
        webDriver.findElement(By.xpath("/html/body/div[6]/div[1]/div[2]/div[2]/div[1]/div/div[3]/a[2]")).click();
        Thread.sleep(1000);

        // Assert that the current page is the cart page
        String currentUrl = webDriver.getCurrentUrl();
        assertTrue(currentUrl.contains("/cart"), "Not on the cart page!");

        // Assert that the correct item is present in the cart
        WebElement cartItem = webDriver.findElement(By.xpath("//span[@class='item__name']"));
        String cartItemName = cartItem.getText();
        assertEquals("Praktik Tools akumulatorska udarna bušilica Flexpower 18V PTQ094 / SOLO", cartItemName,
                "The item in the cart does not match the selected product!");

        // Final assertion to confirm test passed
        System.out.println("Test passed: Correct item is in the cart.");
    }


    @Test
    @Order(3)
    @DisplayName("Test Adding Multiple Items to the Cart")
    public void testAddMultipleItemsToCartWithButton() throws InterruptedException {
        // Navigate to the homepage
        webDriver.get(baseUrl);

        // Navigate to "Alati i Mašine" and select "Aku Bušilice"
        WebElement alatimasine = webDriver.findElement(By.xpath("/html/body/main/header/nav[3]/div/ul[2]/li[7]/span[1]/a"));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(alatimasine).perform();
        Thread.sleep(2000);

        WebElement akubusilice = webDriver.findElement(By.xpath("/html/body/main/header/nav[3]/div/ul[2]/li[7]/div/div/div[1]/ul/li[2]/a"));
        akubusilice.click();
        Thread.sleep(5000);
        WebElement firstProduct = webDriver.findElement(By.xpath("(//div[contains(@class, 'product-item')]//a)[1]"));
        firstProduct.click();
        Thread.sleep(2000);

        webDriver.findElement(By.xpath("/html/body/main/div[5]/div[2]/div[3]/div/div/div[2]/div/div[2]/div/div/div[2]/div[1]/form[2]/button")).click();
        Thread.sleep(1000);

        // Proceed to the cart
        webDriver.findElement(By.xpath("/html/body/div[6]/div[1]/div[2]/div[2]/div[1]/div/div[3]/a[2]")).click();
        Thread.sleep(1000);



        // Navigate back to the "Aku Bušilice" page
        webDriver.get("https://www.ekupi.ba/bs/Alati-i-ma%C5%A1ine/Akumulatorski-ru%C4%8Dni-alati/Aku-bu%C5%A1ilice/c/10844");
        Thread.sleep(2000);

// Find the second product and navigate to its details page
        WebElement secondProduct = webDriver.findElement(By.xpath("//a[@href='/bs/Alati-i-ma%C5%A1ine/Akumulatorski-ru%C4%8Dni-alati/Aku-bu%C5%A1ilice/Klasi%C4%8Dne-aku-bu%C5%A1ilice/Ferm-akumulatorska-bu%C5%A1ilica-odvija%C4%8D-CDM1134-16V%2C-1-5-Ah/p/EK000365304']"));
        secondProduct.click();
        Thread.sleep(2000);

// Locate the "Add to Cart" button using its ID
        // Locate the "Add to Cart" button using XPath and click on it
        WebElement addToCartButton = webDriver.findElement(By.xpath("//button[@id='addToCartButton']"));
        addToCartButton.click();
        Thread.sleep(1000); // Optional: Allow time for action to complete




        //treci
        webDriver.get("https://www.ekupi.ba/bs/Alati-i-ma%C5%A1ine/Akumulatorski-ru%C4%8Dni-alati/Aku-bu%C5%A1ilice/c/10844");
        Thread.sleep(2000);

        WebElement thirdProduct = webDriver.findElement(By.xpath("//a[@href='/bs/Alati-i-ma%C5%A1ine/Akumulatorski-ru%C4%8Dni-alati/Aku-bu%C5%A1ilice/Klasi%C4%8Dne-aku-bu%C5%A1ilice/Ferm-akumulatorska-bu%C5%A1ilica-odvija%C4%8D-CDM1143-20V-SOLO/p/EK000559469']"));
        thirdProduct.click();
        Thread.sleep(2000); // Allow time for the navigation to complete

        webDriver.findElement(By.xpath("/html/body/main/div[5]/div[2]/div[3]/div/div/div[2]/div/div[2]/div/div/div[2]/div[1]/form[2]/button")).click();
        Thread.sleep(1000);

        // Proceed to the cart
        webDriver.findElement(By.xpath("/html/body/div[6]/div[1]/div[2]/div[2]/div[1]/div/div[3]/a[2]")).click();
        Thread.sleep(1000);






// Assert that the products in the cart match the expected names
        WebElement firstCartItem = webDriver.findElement(By.xpath("//span[@class='item__name' and text()='Praktik Tools akumulatorska udarna bušilica Flexpower 18V PTQ094 / SOLO']"));
        assertTrue(firstCartItem.isDisplayed(), "The first product is not in the cart!");

        WebElement secondCartItem = webDriver.findElement(By.xpath("//span[@class='item__name' and text()='Ferm akumulatorska bušilica odvijač CDM1134 / 16V, 1.5 Ah']"));
        assertTrue(secondCartItem.isDisplayed(), "The second product is not in the cart!");

        WebElement thirdCartItem = webDriver.findElement(By.xpath("//span[@class='item__name' and text()='Ferm akumulatorska bušilica odvijač CDM1143 / 20V / SOLO']"));
        assertTrue(thirdCartItem.isDisplayed(), "The third product is not in the cart!");

        System.out.println("Test passed: All ordered products are present in the cart.");
    }


    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
