package Cart;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static Cart.AddToCartWithLogin.wait;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class orderTest {
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
    @DisplayName("Payment")
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


        // Assert that the correct item is present in the cart
        WebElement cartItem = webDriver.findElement(By.xpath("//span[@class='item__name']"));
        String cartItemName = cartItem.getText();


        Thread.sleep(1000);

        WebElement continueCheckoutButton = webDriver.findElement(By.xpath("//button[@class='btn btn-primary btn-block btn--continue-checkout js-continue-checkout-button' and @data-checkout-url='/bs/cart/checkout']"));

        // Scroll the element into view using JavaScript
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});", continueCheckoutButton);

        // Optionally add another small scroll to fine-tune (to center the element better)
        ((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0, -100);");

        // Wait for the element to become clickable
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(continueCheckoutButton));

        // Click the button using JavaScript as a fallback if native click fails
        try {
            continueCheckoutButton.click(); // Native click
        } catch (ElementClickInterceptedException e) {
            // If intercepted, use JavaScript click
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", continueCheckoutButton);
        }

        // Fill in address details
        WebElement titleDropdown = webDriver.findElement(By.id("address.title"));
        Select selectTitle = new Select(titleDropdown);
        selectTitle.selectByValue("g"); // Select "Gospodin"

        WebElement firstNameInput = webDriver.findElement(By.id("address.firstName"));
        firstNameInput.clear();
        slowType(firstNameInput, "John");

        WebElement surnameInput = webDriver.findElement(By.id("address.surname"));
        surnameInput.clear();
        slowType(surnameInput, "Doe");

        WebElement addressLine1Input = webDriver.findElement(By.id("address.line1"));
        addressLine1Input.clear();
        slowType(addressLine1Input, "123 Main Street");

        // Find the "Grad" input field and type "Sarajevo"
        WebElement townCityInput = webDriver.findElement(By.id("address.townCity"));
        townCityInput.clear();
        townCityInput.sendKeys("Sarajevo"); // Start typing the city name

        // Wait for the dropdown option to become visible

        WebElement dropdownOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), '71210 (Sarajevo - Ilidža)')]")));

        // Scroll to the dropdown option if needed
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", dropdownOption);

        // Click on the specific dropdown option
        dropdownOption.click();

        WebElement phoneInput = webDriver.findElement(By.id("address.phone"));
        phoneInput.clear();
        slowType(phoneInput, "+387123456789");

        // Locate the button element
        WebElement addressSubmitButton = webDriver.findElement(By.id("addressSubmit"));

        // Scroll the element into view using JavaScript
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addressSubmitButton);

        // Wait for the element to become clickable

        wait.until(ExpectedConditions.elementToBeClickable(addressSubmitButton));

        // Click the button
        addressSubmitButton.click();

        // Locate the button element
        WebElement deliveryMethodSubmitButton = webDriver.findElement(By.id("deliveryMethodSubmit"));

        // Scroll the button into view using JavaScript
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});", deliveryMethodSubmitButton);

        // Wait for the button to become visible and clickable

        wait.until(ExpectedConditions.visibilityOf(deliveryMethodSubmitButton));
        wait.until(ExpectedConditions.elementToBeClickable(deliveryMethodSubmitButton));

        // Click the button using JavaScript as a fallback if native click fails
        try {
            deliveryMethodSubmitButton.click(); // Attempt native click
        } catch (Exception e) {
            // If native click is intercepted, use JavaScript click
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", deliveryMethodSubmitButton);
        }

        // Locate the button element
        WebElement completeOrderButton = webDriver.findElement(By.id("submit_silentOrderPostForm"));

        // Scroll the button into view using JavaScript
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});", completeOrderButton);

        // Wait for the button to become visible and clickable

        wait.until(ExpectedConditions.visibilityOf(completeOrderButton));
        wait.until(ExpectedConditions.elementToBeClickable(completeOrderButton));

        // Click the button using JavaScript as a fallback if native click fails
        try {
            completeOrderButton.click(); // Attempt native click
        } catch (Exception e) {
            // If native click is intercepted, use JavaScript click
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", completeOrderButton);
        }

        // Wait for the URL to change after clicking the button

        wait.until(ExpectedConditions.urlContains("https://wallet.corvuspay.com/#!/checkout/"));

        // Get the current URL
        String currentUrl = webDriver.getCurrentUrl();

        // Assert that the current URL matches the expected URL
        assertTrue(currentUrl.startsWith("https://wallet.corvuspay.com/#!/checkout/"),
                "The navigation failed or the URL does not match. Current URL: " + currentUrl);
    }






    // Method to simulate slow typing into an input field
    private void slowType(WebElement element, String text) throws InterruptedException {
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            Thread.sleep(100);
        }
    }

    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

}
