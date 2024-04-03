import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import pages.HomePage;
import pages.ProductsPage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

import static java.lang.Double.parseDouble;

public class TC03_Amazon extends BaseTest {
    By low = By.xpath("//input[@id='low-price']");
    By high = By.xpath("//input[@id='high-price']");
    By Fiyatlar = By.xpath("//span[@class=\"a-price\"and @data-a-size=\"xl\"]");
    HomePage homePage;
    ProductsPage productsPage;


    @org.junit.jupiter.api.Test
    @Order(1)
    public void search_a_product() {
        homePage = new HomePage(driver);
        homePage.acceptCookies();
        productsPage = new ProductsPage(driver);
        homePage.searchBox().search("Akıllı Saat");
        Assertions.assertTrue(productsPage.isOnProductPage(),
                "Ürünlerin Sayfasına Ulaşılanamadı");
    }

    @Test
    @Order(2)
    public void price() throws InterruptedException, AWTException {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 450)");

        driver.findElement(low).sendKeys("1000");
        driver.findElement(high).sendKeys("2000");
        driver.findElement(high).click();
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(5000);
    }

    @Test
    @Order(3)
    public void pricefiltre() {

        List<WebElement> productElements = driver.findElements(Fiyatlar);
        boolean allPricesInRange = true;
        for (WebElement productElement : productElements) {
            // Ürün fiyatını al
            WebElement priceElement = productElement.findElement(Fiyatlar);
            String priceText = priceElement.getText();

            // Fiyatı işle
            double price = parsePrice(priceText);

            // Fiyat 1000TL ile 2000TL arasında mı kontrol et
            if (price < 1000 || price > 2000) {
                allPricesInRange = false;
                break;
            }
        }

        // Sonucu yazdır
        if (allPricesInRange) {
            System.out.println("Tüm ürün fiyatları 1000 TL ile 2000 TL arasında.");
        } else {
            System.out.println("Tüm ürün fiyatları 1000 TL ile 2000 TL arasında değil.");
        }


    }
    private double parsePrice(String priceText) {
        String cleanPriceText = priceText.replace("TL", "").trim();
        return parseDouble(cleanPriceText);
    }

}
