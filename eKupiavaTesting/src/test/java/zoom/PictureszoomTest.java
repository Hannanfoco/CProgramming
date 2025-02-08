package zoom;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Go to ekupi.ba
//Locate the search input field and type iphone 16 pro and clik on the search button
//Click on the first iPhone product from the search results.
//save the current url.
//Click on each thumbnail of the phone images to open them.
//Click on the zoom button to zoom into the images.
//Cycle through all images in zoom mode by clicking each one.
//Close the zoom mode by clicking the "X" button at the top right corner.
//Assert that the current url matches the expected url

@Nested
class PictureszoomTest {
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
    void testpictures() throws InterruptedException {

        webDriver.get(baseUrl);
        Thread.sleep(1000);

        webDriver.manage().window().maximize();


        WebElement searchInput = webDriver.findElement(By.xpath("//*[@id=\"js-site-search-input\"]"));
        searchInput.sendKeys("iPhone 16 Pro");
        Thread.sleep(1000);

        WebElement searchButton = webDriver.findElement(By.xpath("//button[@type='submit']"));
        searchButton.click();
        Thread.sleep(3000);


        WebElement firstProduct = webDriver.findElement(By.xpath("(//div[contains(@class, 'product-item')]//a)[1]"));
        firstProduct.click();
        Thread.sleep(3000);


        List<WebElement> imageThumbnails = webDriver.findElements(By.className("binkies-gallery-item-wrapper"));


        for (WebElement thumbnail : imageThumbnails) {
            thumbnail.click();
            Thread.sleep(1500);
        }

        Thread.sleep(1000);




    }
    @Test
    void testZoomIcon() throws  InterruptedException {
        webDriver.get(baseUrl);
        Thread.sleep(1000);

        WebElement searchInput = webDriver.findElement(By.xpath("//*[@id=\"js-site-search-input\"]"));
        searchInput.sendKeys("iPhone 16 Pro");
        Thread.sleep(1000);

        WebElement searchButton = webDriver.findElement(By.xpath("//button[@type='submit']"));
        searchButton.click();
        Thread.sleep(3000);


        WebElement firstProduct = webDriver.findElement(By.xpath("(//div[contains(@class, 'product-item')]//a)[1]"));
        firstProduct.click();
        Thread.sleep(3000);

        String currentUrl = webDriver.getCurrentUrl();

        WebElement zoomButton = webDriver.findElement(By.xpath("/html/body/main/div[5]/div[2]/div[1]/div/div/div[5]/div/div[5]/div/div"));
        zoomButton.click();
        Thread.sleep(2000);

        List<WebElement> zoomedImages = webDriver.findElements(By.className("binkies-gallery-item-wrapper"));
        for (WebElement zoomedImage : zoomedImages) {
            zoomedImage.click();
            Thread.sleep(1000);
        }

        Thread.sleep(2000);


        WebElement closeButton = webDriver.findElement(By.xpath("//*[@id=\"binkies-fullscreen\"]/div/div/div[1]/div/div[4]/div/div/canvas"));
        closeButton.click();
        Thread.sleep(2000);

        String expectedUrl = "https://www.ekupi.ba/bs/Elektronika/Mobiteli-i-dodaci/Mobiteli/Apple-iPhone-16-Pro-128GB-Desert-Titanium%2C-mobitel/p/EK000623206";

        assertEquals(expectedUrl, currentUrl);


    }





    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

}


