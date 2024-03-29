package com.example.produktapi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class SeleniumTest {


    private WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\WebDriver\\bin\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://java22.netlify.app/");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void checkCorrectWebsiteTitle() {
        assertEquals("Webbutik", driver.getTitle(), "Title mismatch!");
    }


    @Test
    void checkAmountOfProducts() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(25));
        List<WebElement> products = driver.findElements(By.className("productItem"));
        assertEquals(20, products.size(), "Amount mismatch!");
    }

    @Test
    public void checkIfPriceIsCorrectOnThreeItems_1() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        String product1 = driver.findElement(By.xpath("//*[@id='productsContainer']/div/div[1]/div/div/p")).getText();
        String product2 = driver.findElement(By.xpath("//*[@id='productsContainer']/div/div[2]/div/div/p")).getText();
        String product3 = driver.findElement(By.xpath("//*[@id='productsContainer']/div/div[3]/div/div/p")).getText();
        String findPriceForProduct1 = "109.95";
        String findPriceForProduct2 = "22.3";
        String findPriceForProduct3 = "55.99";
        boolean product1PriceValidation = product1.contains(findPriceForProduct1);
        boolean product2PriceValidation = product2.contains(findPriceForProduct2);
        boolean product3PriceValidation = product3.contains(findPriceForProduct3);
        assertTrue(product1PriceValidation, "Price mismatch!!");
        assertTrue(product2PriceValidation, "Price mismatch!!");
        assertTrue(product3PriceValidation, "Price mismatch!!");
    }


}
