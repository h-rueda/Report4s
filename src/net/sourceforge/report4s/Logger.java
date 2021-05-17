package net.sourceforge.report4s;

import org.openqa.selenium.WebDriver;

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
 * @author Harmin Parra Rueda
 */
class Logger {

	protected static boolean logging = false;
	private static String description = "";
	private static boolean screenshot;
	private static Type type;

	/**
	 * Log an event.
	 * @param description The description to log.
	 */
	protected static void logEvent(String description) {
		logging = true;
		type = Type.EVENT;
		Logger.description = description;
		Logger.screenshot = false;
	}
		
	/**
	 * Log an event.
	 * @param description The description to log.
	 * @param screenshot Whether to include a full web page screenshot.
	 */
	protected static void logEvent(String description, boolean screenshot) {
		logging = true;
		type = Type.EVENT;
		Logger.description = description;
		Logger.screenshot = screenshot;
	}

	/**
	 * Log a successful event as a PASSED level type.
	 * @param driver The webdriver.
	 */
	protected static void logSuccess(WebDriver driver) {
		if( Logger.logging ) {
			if( Logger.screenshot )
				Report4s.log(Level.PASSED, Logger.description, driver, null, 0);
			else
				Report4s.log(Level.PASSED, Logger.description, null, null, 0);
		}
		Logger.description = "";
		Logger.logging = false;
	}

	/**
	 * Log a failed event as a FAILED level type.
	 * @param error The exception trace.
	 */
	protected static void logFailure(WebDriver driver, Throwable error){
		if( Logger.logging )
			Report4s.log(Level.FAILED, Logger.description, driver, null, 0);
		else
			Report4s.logMessage(Level.ERROR, "Last screenshot before test failure", driver, null, 0);
		Report4s.logTrace(error);
		TestListener.exception_logged = true;
		Logger.description = "";
		Logger.logging = false;
	}
	
	private enum Type { EVENT, ASSERT }
}
