package com.sz.automation.common.selenium.enums;

/**
 * @author Senson S.Kritchker
 * May 10, 2021
 */
public enum BrowserType {

    CHROME              ("webdriver.chrome.driver", "chromedriver.exe"),
    HTML_UNIT_DRIVER    ("",                        ""),
    FIREFOX             ("webdriver.gecko.driver",  "geckodriver.exe"), // geckodriver-v0.19.1-win64 | Firefox Setup 55.0.exe
    INTERNET_EXPLORER   ("webdriver.ie.driver",     "IEDriverServer.exe");

    private final String driver;
    private final String exe;

    BrowserType(String driver, String exe) {
        this.driver = driver;
        this.exe = exe;
    }
    public String getDriver() {
        return driver;
    }
    public String getExe() {
        return exe;
    }
}