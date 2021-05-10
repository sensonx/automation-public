package com.sz.automation.common.selenium;

import com.sz.automation.common.enums.SrcLocation;
import com.sz.automation.common.enums.SysVar;
import com.sz.automation.common.file.FileSrcUtils;
import com.sz.automation.common.jclass.ClassHelper;
import com.sz.automation.common.selenium.enums.BrowserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * @author Senson S.Kritchker
 * May 10, 2021
 */
public class WebDriverFactoryBasic {

    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);

    public static WebDriver createChromeWebDriver(MutableCapabilities capabilities) {
        setDriver(BrowserType.CHROME);
        WebDriver webDriver = new ChromeDriver(getChromeOptions());
        if (capabilities != null) {
            webDriver = new ChromeDriver((ChromeOptions)capabilities);
        }
        return webDriver;
    }

    private static void setDriver(BrowserType browserType) {
        String driverDir = ClassHelper.getProjectPath() + SrcLocation.DRIVER + browserType.getExe();
        logger.info("WebDiver path: {}", driverDir);
        System.setProperty(browserType.getDriver(), driverDir);
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        String tmp = String.format("%schrome", System.getProperty(SysVar.JAVA_TMP));
        FileSrcUtils.deleteDirectoryFiles(tmp);
        String argument = String.format("user-data-dir=%s", tmp);
        chromeOptions.addArguments(argument);
        logger.info("ChromeDriver:ChromeOptions:argument - [{}]", argument);
        return chromeOptions;
    }
}