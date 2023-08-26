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

    /**
     * Log a successful event as a PASSED level type.
     * @param driver The webdriver.
     */
    protected static void logSuccess(String description, WebDriver driver) {
        logSuccess(description, driver, null);
    }

    /**
     * Log a successful event as a PASSED level type.
     * @param driver The webdriver.
     */
    protected static void logSuccess(String description, WebDriver driver, WebElement elem) {
        Report4s.log(Level.PASSED, description, driver, elem);
        TestListener.event_logged = true;
        TestListener.driver = driver;
    }

    /**
     * Log a failed event as a FAILED level type.
     * @param error The exception trace.
     */
    protected static void logFailure(WebDriver driver, Throwable error, String message){
        Report4s.logMessage(Level.FAILED, message, driver, null);
        Report4s.logTrace(error);
    }

}
