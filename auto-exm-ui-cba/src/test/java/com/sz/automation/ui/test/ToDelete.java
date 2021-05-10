package com.sz.automation.ui.test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * @author Senson S.Kritchker
 * May 10, 2021
 */
public class ToDelete {

    private static final Logger logger = LogManager.getLogger(ToDelete.class);
    private String locatorSearchInput = "//input[@id='lst-ib']";
    private String locatorIFL = "//*[@id='sbse0']/div[1]";

    public void searchInput(String value) {
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.google.com/");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        WebElement searchInput = driver.findElement(By.xpath(locatorSearchInput));
        if (searchInput.isEnabled() || searchInput.isDisplayed()) {
            searchInput.click();
            searchInput.sendKeys(value);
            Actions action = new Actions(driver);
            WebElement we = driver.findElement(By.xpath(locatorIFL));
            action.moveToElement(we).click().perform();
        }

        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("c:\\tmp\\screenshot.png"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void testSearch() {
        searchInput("Raanana");

    }

    public static void main(String[] args) {
    }
}