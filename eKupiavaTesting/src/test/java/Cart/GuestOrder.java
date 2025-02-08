package Cart;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;





public class GuestOrder {
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
    public void orderTest() {
        webDriver.get(baseUrl);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));

        // Navigate to "Kućanski aparati"
        WebElement kucanskiAparatiLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Kućanski aparati")));
        kucanskiAparatiLink.click();

        // Scroll to the product element and click it
        WebElement productLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.carousel__item--name")));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", productLink);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", productLink);

        // Scroll to the "Dodaj u košaricu" button
        WebElement addToCartButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("addToCartButton")));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", addToCartButton);

        // Click the "Dodaj u košaricu" button
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();

        // Wait for the "Dovršite kupovinu" button and click it
        WebElement finishPurchaseButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a.btn.btn-primary.btn--continue-shopping[href='/bs/cart']")));
        wait.until(ExpectedConditions.elementToBeClickable(finishPurchaseButton)).click();

        // Assert the URL of the cart page
        String currentUrl = webDriver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.equals("https://www.ekupi.ba/bs/cart"),
                "Test failed: URL did not change to the cart page. Current URL: " + currentUrl);
        System.out.println("Test passed: Navigated to the cart page.");
    }


    @Test
    @Order(2)
    public void testProductNameInCart() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));

        // Verify product name is in the cart
        WebElement productNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.item__name")));
        String actualProductName = productNameElement.getText().trim();
        String expectedProductName = "Candy perilica - sušilica ROW4966DWMCE/1-S";
        assertEquals(expectedProductName, actualProductName,
                "Test failed: Product name in cart does not match. Expected: " + expectedProductName + ", Actual: " + actualProductName);
        System.out.println("Test passed: Product name matches.");
    }

    @Test
    @Order(3)
    public void testProductPriceInCart() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));

        // Verify product price
        WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.current-price")));
        String actualPrice = priceElement.getText().trim(); // Get the text inside the <span>
        String expectedPrice = "829,00 KM"; // Update this to the expected price
        assertEquals(expectedPrice, actualPrice,
                "Test failed: Product price in cart does not match. Expected: " + expectedPrice + ", Actual: " + actualPrice);
        System.out.println("Test passed: Product price matches.");
    }

    @Test
    @Order(4)
    public void testClearCartButton() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));

        // Navigate to the cart page
        webDriver.get(baseUrl + "/bs/cart");

        // Check and add items to the cart if necessary
        List<WebElement> cartItems = webDriver.findElements(By.cssSelector("span.item__name"));
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty. Adding item...");
            webDriver.get(baseUrl);
            WebElement kucanskiAparatiLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Kućanski aparati")));
            kucanskiAparatiLink.click();

            WebElement productLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.carousel__item--name")));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", productLink);

            WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("addToCartButton")));
            addToCartButton.click();

            webDriver.get(baseUrl + "/bs/cart"); // Navigate back to the cart page
        }

        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("window.scrollBy(0, 300);");

        Thread.sleep(2000);

        // Click on "Isprazni košaricu" button
        webDriver.findElement(By.xpath("//*[@id=\"clearCartButton\"]")).click();
        Thread.sleep(3000);

        webDriver.findElement(By.xpath("/html/body/div[6]/div[1]/div[2]/div[2]/div[1]/div/div[2]/div[1]/a")).click();

        Thread.sleep(3000);


        js.executeScript("window.scrollBy(0, 300);");
        Thread.sleep(3000);


        // Assert that the cart is empty
        WebElement emptyCartMessage = webDriver.findElement(By.xpath("/html/body/main/div[5]/div[4]/div[2]/div/p"));
        String emptyCartText = emptyCartMessage.getText();
        assertEquals("Vaša košarica je prazna!", emptyCartText, "Cart is not empty as expected!");

        webDriver.navigate().to(baseUrl);

    }

    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}