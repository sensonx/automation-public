package com.sz.automation.common.selenium;

import com.sz.automation.common.cmd.ExeUtils;
import com.sz.automation.common.enums.SrcLocation;
import com.sz.automation.common.enums.SysVar;
import com.sz.automation.common.file.FileSrcUtils;
import com.sz.automation.common.jclass.ClassHelper;
import com.sz.automation.common.selenium.enums.BrowserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Senson S.Kritchker
 * May 10, 2021
 */
@Service
public class WebDriverFactory {

    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);

//  As XML solution <util:properties id="testDriverProperties" location="classpath:com/qa/automation/common/selenium/qa-selenium-context.properties"/>
//    @Value("#{testDriverProperties['webdriver.wait.implicitly}")
//    private Long WAIT_TIME;
    private static final int WAIT_TIME = 10;

    public WebDriver createChromeWebDriver() {
        return createWebDriver(BrowserType.CHROME);
    }

    public WebDriver createFireFoxWebDriver() {
        return createWebDriver(BrowserType.FIREFOX);
    }

    public WebDriver createIExplorerWebDriver() {
        return createWebDriver(BrowserType.INTERNET_EXPLORER);
    }

    public static void killWebDriverEXE(BrowserType browserType) {
//        String command = String.format("taskkill /im %s /f /t", browserType.getExe());
        String command = String.format("%s%s /t", ExeUtils.KILL, browserType.getExe());
        try {
            logger.info("Executes: [{}]", command);
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public WebDriver createWebDriver(BrowserType browserType) {
        return createWebDriver(browserType, null);
    }

    public WebDriver createWebDriver(BrowserType browserType, MutableCapabilities capabilities) {
        WebDriver webDriver = null;
        switch (browserType) {
            case CHROME:
                setDriver(BrowserType.CHROME);
                webDriver = new ChromeDriver(getChromeOptions());
                if (capabilities != null) {
                    webDriver = new ChromeDriver((ChromeOptions)capabilities);
                }
                break;
            case HTML_UNIT_DRIVER:
                setDriver(BrowserType.FIREFOX);
                webDriver = new HtmlUnitDriver(true);
                if (capabilities != null) {
                    webDriver = new HtmlUnitDriver(capabilities);
                }
                break;
            case FIREFOX:
                setDriver(BrowserType.FIREFOX);
                webDriver = new FirefoxDriver(getFirefoxOptions());
                if (capabilities != null) {
                    webDriver = new FirefoxDriver((FirefoxOptions)capabilities);
                }
                break;
            case INTERNET_EXPLORER:
                setDriver(BrowserType.INTERNET_EXPLORER);
                webDriver = new InternetExplorerDriver(getInternetExplorerOptions());
                if (capabilities != null) {
                    webDriver = new InternetExplorerDriver((InternetExplorerOptions)capabilities);
                }
                break;
        }
        return waitAndMax(webDriver, browserType);
    }

    private void setDriver(BrowserType browserType) {
        String driverDir = ClassHelper.getProjectPath() + SrcLocation.DRIVER + browserType.getExe();
        logger.info("WebDiver path: {}", driverDir);
        System.setProperty(browserType.getDriver(), driverDir);
    }

    private WebDriver waitAndMax(WebDriver driver, BrowserType browserType) {
        driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        logger.info("Successfully initialised '{}' browser", browserType);
        return driver;
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        String tmp = String.format("%schrome", System.getProperty(SysVar.JAVA_TMP));
        FileSrcUtils.deleteDirectoryFiles(tmp);
        String argument = String.format("user-data-dir=%s", tmp);
        chromeOptions.addArguments(argument);
        logger.info("ChromeDriver:ChromeOptions:argument - [{}]", argument);
        return chromeOptions;
    }

    private FirefoxOptions getFirefoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setCapability(FirefoxDriver.PROFILE, new FirefoxProfile());//getTestProfile());
        firefoxOptions.setCapability("marionette", true);
        return firefoxOptions;
    }

    private InternetExplorerOptions getInternetExplorerOptions() {
        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
//        ieOptions.setCapability(CapabilityType.VERSION, "EDGE");
        ieOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        ieOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        ieOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        ieOptions.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        return ieOptions;
    }

//    private FirefoxProfile getTestProfile() {
//        FirefoxProfile profile = new FirefoxProfile();
//        profile.setEnableNativeEvents(true);
//        return profile;
//    }
}