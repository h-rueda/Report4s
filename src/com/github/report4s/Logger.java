package com.github.report4s;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * The logger of web browser events.
 * The events that are taken into account are :
 * 
 * Select.deselectByIndex(int)
 * Select.deselectByValue(java.lang.String)
 * Select.deselectByVisibleText(java.lang.String)
 * 
 * Select.selectByIndex(int)
 * Select.selectByValue(java.lang.String)
 * Select.selectByVisibleText(java.lang.String)
 * 
 * WebDriver.get(java.lang.String)
 * 
 * WebDriver.navigate().back()
 * WebDriver.navigate().forward()
 * WebDriver.navigate().refresh()
 * WebDriver.navigate().to(java.lang.String)
 * WebDriver.navigate().to(java.net.URL)
 * 
 * WebElement.click()
 * WebElement.sendKeys(CharSequence[])
 * 
 */
class Logger {

    protected static boolean logging = false;
    protected static WebDriver driver = null;
    private static String description = "";

    /**
     * Log an event.
     * @param description The description to log.
     */
    protected static void logEvent(String description) {
        logging = true;
        Logger.description = description;
    }

    /**
     * Log an event.
     * @param description The description to log.
     * @param screenshot Whether to include a full web page screenshot.
     */
    protected static void logEvent(String description, boolean screenshot) {
        logging = true;
        Logger.description = description;
    }

    /**
     * Log a successful event as a PASSED level type.
     * @param driver The webdriver.
     */
    protected static void logSuccess(WebDriver driver) {
        logSuccess(driver, null);
    }

    /**
     * Log a successful event as a PASSED level type.
     * @param driver The webdriver.
     */
    protected static void logSuccess(WebDriver driver, WebElement elem) {
        if (Logger.logging)
            Report4s.log(Level.PASSED, Logger.description, driver, elem);
        Logger.description = "";
        Logger.logging = false;
    }

    /**
     * Log a failed event as a FAILED level type.
     * @param error The exception trace.
     */
    protected static void logFailure(WebDriver driver, Throwable error, String message){
        if (Logger.logging)
            Report4s.log(Level.FAILED, Logger.description, driver, null);
        else
            Report4s.logMessage(Level.FAILED, message, driver, null);
        Report4s.logTrace(error);
        Logger.description = "";
        Logger.logging = false;
    }

}
