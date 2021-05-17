package net.sourceforge.report4s;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import java.awt.image.*;
import org.apache.commons.io.*;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.*;
import org.testng.*;
import org.testng.xml.*;

/**
 * Utility class.<br>
 * @author Harmin Parra Rueda
 */
class Utils {

	/**
	 * Counter used in order to name screenshot files.
	 */
	private static int screenshotCount = 1;

	/**
	 * Return the current time in milliseconds.
	 * @return the current time in milliseconds.
	 */
	protected static long getTimeInMillisec() {
		return Calendar.getInstance().getTimeInMillis();
	}
	
	/**
	 * Return the execution time label in seconds with the number of decimals specified in the Report4s.precision static attribute.
	 * @param startTime The start execution time of a test.
	 * @param endTime The end execution time of a test.
	 * @return The execution time label formated with the desired decimal precision.
	 */
	protected static String getExecutionLabel(long startTime, long endTime) {
		return String.format("%." + Report4s.time_precision + "f", ((float)(endTime - startTime))/1000) + " seconds";
	}
	
	/**
	 * Return the execution time label in seconds with the number of decimals specified in the report4s.properties file.
	 * @param time The execution time of a test.
	 * @return The execution time label formated with the desired decimal precision..
	 */
	protected static String getExecutionLabel(long time) {
		return getExecutionLabel(0, time);
	}
	
	/**
	 * Return the description attribute of a test method.
	 * @param result The test under execution.
	 * @return The description attribute of the @test annotation of the test method.
	 */
	protected static String getDescription(ITestResult result) {
		return result.getMethod().getDescription();
	}

	/**
	 * Return the class name of a method.
	 * @param result The test under execution.
	 * @return The class name.
	 */
	protected static String getClazz(ITestResult result) {
		return result.getMethod().getRealClass().getName();
	}

	/**
	 * Return the method name of a method.
	 * @param result The test under execution.
	 * @return The method name of the executed method.
	 */
	protected static String getMethod(ITestResult result) {
		return result.getMethod().getMethodName();
	}
	
	/**
	 * Return the parameters of a test method.
	 * @param result The test under execution.
	 * @return The parameters.
	 */
	protected static String getParameters(ITestResult result) {
		Object[] parameters = result.getParameters();
		String parameter = java.util.Arrays.deepToString(parameters);
		if( parameters != null && parameters.length > 0 )
			return parameter.substring(1,  parameter.length()-1);
		else
			return null;
	}

	/**
	 * Return the success percentage of a test method.
	 * @param result The test under execution.
	 * @return The successPercentage attribute of the @test annotation.
	 */
	protected static int getSuccessPercentage(ITestResult result) {
		return result.getMethod().getSuccessPercentage();
	}
	
	/**
	 * Return the status of a {test|configuration} execution.
	 * @param result The {test|configuration} under execution.
	 * @return The status.
	 */
	protected static Status getTestStatus(ITestResult result) {
		switch(result.getStatus()) {
			case ITestResult.SUCCESS : return Status.PASSED;
			case ITestResult.SKIP : return Status.SKIPPED;
			case ITestResult.FAILURE : return Status.FAILED;
			case ITestResult.SUCCESS_PERCENTAGE_FAILURE : return Status.FAILED_WITHIN_PERCENTAGE;
			default : return null;
		}
	}
	
	/**
	 * Return the annotation of a configuration method.
	 * @param result The configuration under execution.
	 * @return The annotation of the configuration method.
	 */
	protected static String getConfigurationAnnotation(ITestResult result) {
		if( result.getMethod().isAfterClassConfiguration() )
			return "@AfterClass";
		if( result.getMethod().isAfterGroupsConfiguration() )
			return "@AfterGroups";
		if( result.getMethod().isAfterMethodConfiguration() )
			return "@AfterMethod";
		if( result.getMethod().isAfterSuiteConfiguration() )
			return "@AfterSuite";
		if( result.getMethod().isAfterTestConfiguration() )
			return "@AfterTest";
		if( result.getMethod().isBeforeClassConfiguration() )
			return "@BeforeClass";
		if( result.getMethod().isBeforeGroupsConfiguration() )
			return "@BeforeGroups";
		if( result.getMethod().isBeforeMethodConfiguration() )
			return "@BeforeMethod";
		if( result.getMethod().isBeforeSuiteConfiguration() )
			return "@BeforeSuite";
		if( result.getMethod().isBeforeTestConfiguration() )
			return "@BeforeTest";
		else
			return null;
	}

	/**
	 * Return the html <img> tag and label corresponding to the log level.
	 * @param level The log level.
	 * @return The html <img> tag and label of the log level.
	 */
	protected static String getIconTag(Level level) {
		String tag = "";
		switch (level) {
			case PASSED :
				tag = "<img src='resources/img/logpass.png' class='icon'>&nbsp;<i>PASSED</i>";
				break;
			case FAILED :
				tag = "<img src='resources/img/logerror.png' class='icon'>&nbsp;<i>FAILED</i>";
				break;
			case ERROR :
				tag = "<img src='resources/img/logerror.png' class='icon'>&nbsp;<i>ERROR</i>";
				break;
			case INFO :
				tag = "<img src='resources/img/loginfo.png' class='icon'>&nbsp;<i>INFO</i>";
				break;
			case WARNING :
				tag = "<img src='resources/img/logwarning.png' class='icon'>&nbsp;<i>WARNING</i>";
				break;
			case DEBUG :
				tag = "<img src='resources/img/logdebug.png' class='icon'>&nbsp;<i>DEBUG</i>";
				break;
			case TRACE :
				tag = "<img src='resources/img/loupe.png' class='icon'>&nbsp;<i>TRACE</i>";
				break;
			case UNKNOWN :
				tag = "<img src='resources/img/unknown.png' class='icon'>&nbsp;<i>UNKNOWN</i>";
				break;
		}
		return tag;
	}

	/**
	 * Get the html <tr> tag of a log.
	 * @param icon The content of the html <td> tag of the icon cell.
	 * @param message The message to be logged.
	 * @param link The html <img> tag of the screenshot.
	 * @return The html <tr> tag of the log.
	 */
	protected static String getLogTag(String icon, String message, String link) {
		if( link == null )
			link = "";
		
		String log = 
			"    <tr>" + "\n" +
			"      <td>" + icon + "</td>" + "\n" +
			"      <td>" + message + "</td>" + "\n" +
			"      <td align='center'>" + link + "</td>" + "\n" +
			"    </tr>";
		return log;
	}

	/**
	 * Get the html <tr> tag of a exception trace.
	 * @param error The exception trace.
	 * @param traceCount The id used by the JavaScript to expand/collapse the trace details.
	 * @return The html <tr> tag of the exception trace.
	 */
	protected static String getTraceTag(Throwable error, int traceCount) {
		String log =
			"    <tr>" + "\n" +
			"      <td style='vertical-align:top'>" + "\n" +
			"        " + Utils.getIconTag(Level.TRACE) + "\n" +
			"        <img src='resources/img/expand.png' class='plusminus' id='expand_trace_" + traceCount + "' onclick='expand_trace(" + traceCount + ")'>" + "\n" +
			"        <img src='resources/img/collapse.png' class='plusminus' id='collapse_trace_" + traceCount + "' onclick='collapse_trace(" + traceCount + ")' style='display:none'>" + "\n" +
			"      </td>" + "\n" +
			"      <td colspan='2'>" + "\n" +
			"        <span class='trace'>" + "\n" +
			"          " + Utils.getMessage(error) + "<br>" + "\n" +
			"          <div id='trace_" + traceCount + "' style='display:none'>" + "\n";
		for( StackTraceElement ste : error.getStackTrace() )
			log +=
			"            " + ste.toString() + "<br>" + "\n";
		log +=
			"          </div>" + "\n" +
			"        </span>" + "\n" +
			"      </td>" + "\n" +
			"    <tr>" + "\n";
		
		return log;
	}
	
	/**
	 * Return the html <a> tag of the appropriate screenshot.
	 * If element = null, the screenshot will correspond to the full web page.
	 * Otherwise, the screenshot will correspond to the web element.
	 * @param level The log level.
	 * @param driver The WebDriver object.
	 * @param element The WebElement object.
	 * @param padding The padding to be applied to the WebElement screenshot.
	 * @return The html <a> tag of the screenshot.
	 */
	protected static String getScreenshotTag(Level level, WebDriver driver, WebElement element, int padding) {
		String link = "";
		if( Report4s.screenshots || (level == Level.ERROR) || (level == Level.FAILED) ) {
			if( element != null )
				link = Utils.getWebElementScreenshotTag(driver, element, padding);
			else if( driver != null )
				link = Utils.getWebPageScreenshotTag(driver);
		}
		return link;
	}

	/**
	 * Return the html <a> tag of a full web page screenshot.
	 * @param driver The WebDriver object.
	 * @return The html <a> tag of the full web page screenshot.
	 */
	private static String getWebPageScreenshotTag(WebDriver driver) {
		String tag = null;
		if( driver != null && !(driver instanceof HtmlUnitDriver) ) {
			try {
				//Get the page screenshot
				File screen = ((TakesScreenshot)(driver)).getScreenshotAs(OutputType.FILE);
				//Create the screenshot file
				String folder = Report4s.report_dir + File.separator + "screenshots" + File.separator;
				String file = "image-" + screenshotCount++ + ".png";
				FileUtils.copyFile(screen, new File(folder + file));
				//Set the <a> html tag
				String file2 = "screenshots/" + file;
				tag = "<a href='" + file2 + "' target='_blank'><img src='" + file2 + "' class='screenshot'></a>";
			} catch (Exception e) {
				System.err.println("Failed to take and save screenshot");
			}
		}
		return tag;
	}

	/**
	 * Return the html <a> tag of a web element screenshot.
	 * @param driver The WebDriver object.
	 * @param element The WebElement object.
	 * @padding The padding to apply to the web element.
	 * @return The html <a> tag of the web element screenshot.
	 */
	private static String getWebElementScreenshotTag(WebDriver driver, WebElement element, int padding) {
		String tag = null;
		File screen = null;
		if( element != null ) {
			try {
				//Take screenshot of a HtmlUnitWebElement instance
				if( element instanceof HtmlUnitWebElement )
					screen = ((TakesScreenshot)(element)).getScreenshotAs(OutputType.FILE);
				//Take screenshot for other WebElement instances
				if( driver != null && !(element instanceof HtmlUnitWebElement) ) {
					//Get the page screenshot
					screen = ((TakesScreenshot)(driver)).getScreenshotAs(OutputType.FILE);
					BufferedImage  img = ImageIO.read(screen);
					//Get the location of the element
					Point point = element.getLocation();
					//Get width and height of the element
					int width = element.getSize().getWidth();
					int height = element.getSize().getHeight();
					//Crop the entire page screenshot to get only the element screenshot
					BufferedImage crop = img.getSubimage(
									point.getX() - padding < 0 ? 0 : point.getX() - padding, 
									point.getY() - padding < 0 ? 0 : point.getY() - padding,
									width + 2*padding,
									height + 2*padding);
					ImageIO.write(crop, "png", screen);
				}
				//Create the screenshot file
				String folder = Report4s.report_dir + File.separator + "screenshots" + File.separator;
				String file = "image-" + screenshotCount++ + ".png";
				FileUtils.copyFile(screen, new File(folder + file));
				//Set the <a> html tag
				String file2 = "screenshots/" + file;
				tag = "<a href='" + file2 + "' target='_blank'><img src='" + file2 + "' class='screenshot'></a>";
			} catch (Exception e) {
				System.err.println("Failed to capture screenshot");
			}
		}

		if( element == null || driver == null )
			System.err.println("Failed to take screenshot because either the webdriver or webelement is null");
		
		return tag;
	}

	/**
	 * Return the message of a throwable exception.
	 * @param error The throwable exception.
	 */
	protected static String getMessage(Throwable error) {
		return error.toString().split("\n")[0];
	}
	
	/**
	 * Whether a suite has multi-threaded tests.
	 * @param suite The suite under execution.
	 * @return True if the suite has multi-threaded tests. False otherwise.
	 */
	protected static boolean hasMultiThreadedTests(ISuite suite) {
		XmlSuite xml_suite = suite.getXmlSuite();
		//Let's check the <suite> tag attributes
		if( xml_suite.getThreadCount() > 1 && !(xml_suite.getParallel().equals("none") || xml_suite.getParallel().equals("false")) )
			return true;
		
		//Now let's check the nested <test> tag attributes
		for( XmlTest xml_test : xml_suite.getTests() )
			if( xml_test.getThreadCount() > 1 && !(xml_test.getParallel().equals("none") || xml_test.getParallel().equals("false")) )
				return true;
		
		return false;
	}
	
	/**
	 * Return the JavaScript declaration code of a piechart data.
	 * @param var The name of the JavaScript variable to hold the data.
	 * @param passed The number of passed tests.
	 * @param failed The number of failed tests.
	 * @param failed_pct The number of failed within success percentage tests.
	 * @param skipped The number of skipped tests.
	 * @return The JavaScript declaration code of the piechart data.
	 */
	protected static String getPiechartData(String var, int passed, int failed, int failed_pct, int skipped) {
		String content =
			"var "+ var + " = [" + " \n" +
			"	{" + " \n" +
			"		value: " + passed + "," + "\n" +
			"		color: '#33CC33'," + "\n" +
			"		highlight: '#8ADD6D'," + "\n" +
			"		label: 'Passed'" + "\n" +
			"	}," + "\n" +
			"	{" + "\n" +
			"		value: " + failed + "," + "\n" +
			"		color:'#FF3333'," + "\n" +
			"		highlight: '#FF8080'," + "\n" +
			"		label: 'Failed'" + "\n" +
			"	}," + "\n";				
		if( failed_pct > 0 ) {
			content += 
			"	{" + "\n" +
			"		value: " + failed_pct + "," + "\n" +
			"		color:'#FFCC00'," + "\n" +
			"		highlight: '#FFE066'," + "\n" +
			"		label: 'Failed within success %'" + "\n" +
			"	}," + "\n";
		}
			content += 
			"	{" + "\n" +
			"		value: " + skipped + "," + "\n" +
			"		color: '#66B2FF'," + "\n" +
			"		highlight: '#B3D9FF'," + "\n" +
			"		label: 'Skipped'" + "\n" +
			"	} ];" + "\n\n";
		return content;
	}

}
