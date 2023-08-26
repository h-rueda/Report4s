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
    private String currentUrl = null;
    private String elem_tag = null;


    public void afterAnyNavigationCall​(WebDriver.Navigation navigation,
                                       java.lang.reflect.Method method,
                                       java.lang.Object[] args,
                                       java.lang.Object result) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            if (StringUtils.equals(method.getName(), "to"))
                Logger.logEvent(decorate("Navigate " + method.getName(), "action") + " " + decorate((String)args[0], "target"));
            else
                Logger.logEvent(decorate("Navigate " + method.getName(), "action"));
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
            Logger.logEvent(decorate("Clear", "action") + " element " + getWebElementAttributes(element));
            Logger.logSuccess(this.driver, element);
        }
    }

    public void afterClick​(WebElement element) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            if (!StringUtils.equals(driver.getCurrentUrl(), this.currentUrl)) {
                Logger.logEvent(decorate("Click", "action") + " element " + this.elem_tag);
                Logger.logSuccess(this.driver, null);
                this.currentUrl = driver.getCurrentUrl();
            } else
                Logger.logEvent(decorate("Click", "action") + " element " + getWebElementAttributes(element));
                Logger.logSuccess(this.driver, element);
            this.elem_tag = null;
        }
    }

    public void afterFindElement​(WebDriver driver, By locator, WebElement result) { }
    
    public void afterFindElement​(WebElement element, By locator, WebElement result) { }

    public void afterFindElements​(WebDriver driver, By locator, WebElement result) { }

    public void afterFindElement​s(WebElement element, By locator, WebElement result) { }

    public void afterSendKeys​(WebElement element, java.lang.CharSequence... args) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            Logger.logEvent(decorate("SendKeys", "action") + " " + getQuotation() + args[0] + getQuotation() + " to element " + getWebElementAttributes(element));
            Logger.logSuccess(this.driver, element);
        }
    }

    public void afterClose​(WebDriver driver) {
        this.driver = null;
        Logger.driver = null;
    }

    public void afterQuit(WebDriver driver) {
        this.afterClose(driver);
    }

    public void beforeClick(WebElement element) {
        this.elem_tag = getWebElementAttributes(element);
    }

    public void beforeFindElement​(WebDriver driver, By locator) { }

    public void beforeFindElement​(WebElement element, By locator) { }

    public void beforeFindElements​(WebDriver driver, By locator) { }

    public void beforeFindElements​(WebElement element, By locator) { }

    public void beforeGet​(WebDriver driver, java.lang.String url) {
        if (StringUtils.equals(Report4s.screenshots, "all")) {
            this.driver = driver;
            Logger.logEvent(decorate("Get", "action") + " " + decorate(url, "target"));
        }
    }

    public void onError​(java.lang.Object target,
            java.lang.reflect.Method method,
            java.lang.Object[] args,
            java.lang.reflect.InvocationTargetException e) {
        Logger.logFailure(driver, e.getCause(), "Selenium Error");
    }
    
    private String getWebElementAttributes(WebElement element) {
        String tag = element.getTagName();
        String id = element.getDomAttribute("id");
        String name = element.getDomAttribute("name");
        String type = element.getDomAttribute("type");
        String value = element.getDomAttribute("value");
        String checked = element.getAttribute("checked");
        String classes = element.getDomAttribute("class");
        String href = element.getDomAttribute("href");
        String text = element.getText();
        
        String result = "&lt;";
        if (tag != null)
            result += tag;
        if (!StringUtils.isEmpty(href))
            result += " href=\"" + href + "\"";
        if (!StringUtils.isEmpty(type))
            result += " type=\"" + type + "\"";
        if (!StringUtils.isEmpty(id))
            result += " id=\"" + id + "\"";
        if (!StringUtils.isEmpty(name))
            result += " name=\"" + name + "\"";
        if (!StringUtils.isEmpty(value) &&
                !(StringUtils.equals(type, "text") || (StringUtils.equals(type, "textarea"))))
            result += " value=\"" + value + "\"";
        if (!StringUtils.isEmpty(classes))
            result += " class=\"" + classes + "\"";
        if (!StringUtils.isEmpty(text))
            result += " text=\"" + text + "\"";
        if (StringUtils.equals(checked, "true"))
            result += " checked";
        result += "&gt;";
        return decorate(result, "target");
    }
    
    private String decorate(String label, String cssclass) {
        return "<span class=\"" + cssclass + "\">" + label + "</span>";
    }
    
    private String getQuotation() {
        return decorate("\"", "quotation");
    }

}
