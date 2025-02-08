package org.example;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EkupiHomepageTests  {

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
    public void testHomepageTitle() {

        webDriver.get("https://www.ekupi.ba");


        String pageTitle = webDriver.getTitle();
        assertEquals("eKupi.ba - Vaša internet trgovina: Najbolje ponude i cijene!", pageTitle);
    }




    @Test
    public void testPageSourceContainsKeyword() {

        webDriver.get("https://www.ekupi.ba");


        String pageSource = webDriver.getPageSource();
        assertTrue("Page source does not contain the expected keyword!", pageSource.contains("ekupi"));
    }

    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }



}
