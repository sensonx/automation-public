package com.sz.automation.ui.test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.sz.automation.common.selenium.WebDriverFactoryBasic;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * @author Senson S.Kritchker
 * May 10, 2021
 */
public class GoogleSearchTest {
    // Take screenshot: https://stackoverflow.com/questions/3422262/take-a-screenshot-with-selenium-webdriver
    // Mouse move & click: https://stackoverflow.com/questions/17293914/how-to-perform-mouseover-function-in-selenium-webdriver-using-java

    private static final Logger logger = LogManager.getLogger(GoogleSearchTest.class);
    private WebDriver driver;
    private String locatorSearchInput = "//input[@title='Search']";
//    private String locatorSearchInput = "//input[@name='q']";
    private String locatorListItem = "//span[contains(normalize-space(), \"%s\")]";

    @After
    public void cleanSteps() {
        if (driver != null) {
            driver.close();
        }
    }

    public void searchInput(String value) {
        driver = WebDriverFactoryBasic.createChromeWebDriver(null);//new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://www.google.com/");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        WebElement searchInput = driver.findElement(By.xpath(locatorSearchInput));
        if (searchInput.isEnabled() || searchInput.isDisplayed()) {
            searchInput.click();
            searchInput.sendKeys(value);
            Actions action = new Actions(driver);
            locatorListItem = String.format(locatorListItem, value.toLowerCase());
            WebElement webElement = null;
            try {
                webElement = driver.findElement(By.xpath(locatorListItem));
                if (webElement == null) {
                    Thread.sleep(1000);
                    webElement = driver.findElement(By.xpath(locatorListItem));
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            action.moveToElement(webElement).click().perform();
        }

        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("d:\\tmp\\screenshot" + System.currentTimeMillis() + ".png"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void testSearch() {
        searchInput("DocAuthority");
    }
}