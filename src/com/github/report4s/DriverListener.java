package com.github.report4s;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.apache.commons.lang3.StringUtils;

/**
 * Implementation of the WebDriverListener interface.
 */
public class DriverListener implements WebDriverListener {

    private WebDriver driver = null;
    private By locator = null;
    private boolean finding = false;
    private String currentUrl = null;
    

    public void afterAnyNavigationCall​(WebDriver.Navigation navigation,
                                       java.lang.reflect.Method method,
                                       java.lang.Object[] args,
                                       java.lang.Object result) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            if (StringUtils.equals(method.getName(), "to"))
                Logger.logEvent("Navigate " + method.getName() + " " + args[0], true);
            else
                Logger.logEvent("Navigate " + method.getName(), true);
            Logger.logSuccess(this.driver);
        }
    }

    public void afterAnyWebDriverCall​(WebDriver driver,
                                      java.lang.reflect.Method method,
                                      java.lang.Object[] args,
                                      java.lang.Object result) {
        this.driver = driver;
        Logger.driver = driver;
        if (driver != null
                && !StringUtils.equals(method.getName(), "close")
                && !StringUtils.equals(method.getName(), "quit"))
            this.currentUrl = driver.getCurrentUrl();
    }

    public void afterGet​(WebDriver driver, java.lang.String url) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            this.driver = driver;
            this.currentUrl = driver.getCurrentUrl();
            Logger.logSuccess(this.driver);
        }
    }

    public void afterClear(WebElement element) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            Logger.logEvent("Clear " + this.locator, true);
            Logger.logSuccess(this.driver, element);
        }
    }

    public void afterClick​(WebElement element) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            Logger.logEvent("Click " + this.locator, true);
            if (!StringUtils.equals(driver.getCurrentUrl(), this.currentUrl)) {
                this.currentUrl = driver.getCurrentUrl();
                Logger.logSuccess(this.driver, null);
            } else
                Logger.logSuccess(this.driver, element);
        }
    }

    public void afterFindElement​(WebDriver driver, By locator, WebElement result) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            this.locator = locator;
            this.finding = false;
        }
    }
    
    public void afterFindElement​(WebElement element, By locator, WebElement result) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            this.locator = locator;
            this.finding = false;
        }
    }

    public void afterFindElements​(WebDriver driver, By locator, WebElement result) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            this.locator = locator;
            this.finding = false;
        }
    }

    public void afterFindElement​s(WebElement element, By locator, WebElement result) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            this.locator = locator;
            this.finding = false;
        }
    }

    public void afterSendKeys​(WebElement element, java.lang.CharSequence... args) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            Logger.logEvent("SendKeys \"" + args[0] + "\"", true);
            Logger.logSuccess(this.driver, element);
        }
    }

    public void afterClose​(WebDriver driver) {
        this.driver = null;
        this.locator = null;
        this.finding = false;
        Logger.driver = null;
    }

    public void afterQuit(WebDriver driver) {
        this.afterClose(driver);
    }

    public void beforeFindElement​(WebDriver driver, By locator) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
           this.finding = true;
           this.locator = locator;
           Logger.logEvent("FindElement " + this.locator);
        }
    }

    public void beforeFindElement​(WebElement element, By locator) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            this.finding = true;
            this.locator = locator;
            Logger.logEvent("FindElement " + this.locator);
        }
    }

    public void beforeFindElements​(WebDriver driver, By locator) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            this.finding = true;
            this.locator = locator;
            Logger.logEvent("FindElements " + this.locator);
        }
    }

    public void beforeFindElements​(WebElement element, By locator) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            this.finding = true;
            this.locator = locator;
            Logger.logEvent("FindElements " + this.locator);
        }
    }

    public void beforeGet​(WebDriver driver, java.lang.String url) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            this.driver = driver;
            Logger.logEvent("Get " + url, true);
        }
    }

    public void onError​(java.lang.Object target,
            java.lang.reflect.Method method,
            java.lang.Object[] args,
            java.lang.reflect.InvocationTargetException e) {
        if (this.finding)
        	this.finding = false;
        Logger.logFailure(driver, e.getCause(), "Selenium Error");
    }

}
