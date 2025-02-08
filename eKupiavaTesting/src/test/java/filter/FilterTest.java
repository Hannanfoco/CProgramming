package filter;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterTest {
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

    //Testing the Samsung filter and asserting that the new page shows only Samsung phones.

    @Test
    public void testsingleFilter() throws InterruptedException {
        webDriver.get(baseUrl);
        webDriver.manage().window().maximize();


        WebElement elektronikaLink = webDriver.findElement(By.xpath("//a[contains(text(), 'Elektronika')]"));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(elektronikaLink).perform();
        Thread.sleep(2000);


        WebElement mobiteliLink = webDriver.findElement(By.xpath("/html/body/main/header/nav[3]/div/ul[2]/li[4]/div/div/div[2]/ul/li[1]/a"));
        mobiteliLink.click();
        Thread.sleep(2000);

        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("window.scrollBy(0, 300);");

        Thread.sleep(2000);

        WebElement samsungCheckbox = webDriver.findElement(By.xpath("/html/body/main/div[5]/div[2]/div[1]/div/div/div/div[3]/div[2]/ul/li[2]/label/span/span[2]/a"));
        js.executeScript("arguments[0].scrollIntoView(true);", samsungCheckbox);

        Thread.sleep(1000);

        samsungCheckbox.click();


        Thread.sleep(3000);


        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");


        Thread.sleep(2000);

        js.executeScript("window.scrollTo(0, 0);");


        Thread.sleep(2000);


        List<WebElement> productTitles = webDriver.findElements(By.xpath("//div[@class='product-item']//div[@class='name']"));

        for (WebElement title : productTitles) {
            String productTitleText = title.getText().toLowerCase();

            assertTrue(productTitleText.contains("samsung"),
                    "Expected the product title to contain 'Samsung', but found: " + productTitleText);

        }
    }
    //Test DoubleFilter
    //Testing Double Filter, where Apple and the price range are selected,
    //Sorting the page by the most expensive phone
    //Asserting that the most exprensive phone is Iphone 16 pro max 1TB.

    @Test
    void doublefilter() throws InterruptedException {

        webDriver.get(baseUrl);
        webDriver.findElement(By.xpath("/html/body/main/header/nav[1]/div/div[1]/div/div/div/a/img")).click();
        Thread.sleep(1000);

        WebElement elektronikaLink = webDriver.findElement(By.xpath("//a[contains(text(), 'Elektronika')]"));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(elektronikaLink).perform();
        Thread.sleep(2000);


        WebElement mobiteliLink = webDriver.findElement(By.xpath("/html/body/main/header/nav[3]/div/ul[2]/li[4]/div/div/div[2]/ul/li[1]/a"));
        mobiteliLink.click();
        Thread.sleep(2000);

        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("window.scrollBy(0, 300);");


        Thread.sleep(2000);

        WebElement AppleCheckbox = webDriver.findElement(By.xpath("/html/body/main/div[5]/div[2]/div[1]/div/div/div/div[3]/div[2]/ul/li[1]/label/span/span[2]/a"));


        Thread.sleep(1000);

        AppleCheckbox.click();


        Thread.sleep(3000);

        js.executeScript("window.scrollBy(0, 300);");

        Thread.sleep(2000);

        WebElement price = webDriver.findElement(By.xpath("/html/body/main/div[5]/div[2]/div[1]/div/div/div/div[5]/div[2]/ul/li/form/label/span"));
        js.executeScript("arguments[0].scrollIntoView(true);", price);
        Thread.sleep(1000);

        price.click();

        Thread.sleep(2000);

        WebElement sort = webDriver.findElement(By.xpath("/html/body/main/div[5]/div[2]/div[2]/div/div/div[1]/div[1]/div[2]/div/div[1]/div[1]/form/select"));
        sort.click();
        Thread.sleep(2000);

        Select sortiranje = new Select(sort);
        sortiranje.selectByVisibleText("Cijena (prvo najviša)");

        Thread.sleep(1000);

        WebElement iphone16 = webDriver.findElement(By.xpath("/html/body/main/div[5]/div[2]/div[2]/div/div/div[2]/div[1]/div[1]/div/a"));

        assertEquals("Apple iPhone 16 Pro Max 1TB Black Titanium, mobitel", iphone16.getText());

        Thread.sleep(1000);

        webDriver.navigate().to(baseUrl);
        Thread.sleep(1000);
    }




    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

}