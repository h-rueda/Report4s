package net.sourceforge.report4s;

import org.openqa.selenium.*;
import org.openqa.selenium.support.events.WebDriverEventListener;
import net.sourceforge.report4s.Logger;

/**
 * Implementation of the WebDriverEventListener interface.
 * @author Harmin Parra Rueda
 */
public class EventHandler implements WebDriverEventListener {

	public void afterChangeValueOf(WebElement elem, WebDriver driver) {	}

	public void afterClickOn(WebElement elem, WebDriver driver) {
		Logger.logSuccess(driver);
	}

	public void afterFindBy(By by, WebElement elem, WebDriver driver) {	}

	public void afterNavigateBack(WebDriver driver) {
		Logger.logSuccess(driver);
	}

	public void afterNavigateForward(WebDriver driver) {
		Logger.logSuccess(driver);
	}

	public void afterNavigateTo(String url, WebDriver driver) {
		Logger.logSuccess(driver);
	}

	public void afterScript(String script, WebDriver driver) { }

	public void beforeChangeValueOf(WebElement elem, WebDriver driver) { }

	public void beforeClickOn(WebElement elem, WebDriver driver) { }

	public void beforeFindBy(By by, WebElement arg1, WebDriver driver) { }

	public void beforeNavigateBack(WebDriver driver) { }

	public void beforeNavigateForward(WebDriver driver) { }

	public void beforeNavigateTo(String url, WebDriver driver) { }

	public void beforeScript(String script, WebDriver driver) { }

	public void onException(Throwable error, WebDriver driver) {
		Logger.logFailure(driver, error);
	}

	public void afterAlertAccept(WebDriver driver) { }

	public void afterAlertDismiss(WebDriver driver) { }

	public void afterChangeValueOf(WebElement elem, WebDriver driver, CharSequence[] value) {
		Logger.logSuccess(driver);
	}

	public void afterNavigateRefresh(WebDriver driver) { 
		Logger.logSuccess(driver);
	}

	public void beforeAlertAccept(WebDriver driver) { }

	public void beforeAlertDismiss(WebDriver driver) { }

	public void beforeChangeValueOf(WebElement elem, WebDriver driver, CharSequence[] value) { }

	public void beforeNavigateRefresh(WebDriver driver) { }

}

