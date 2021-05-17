package net.sourceforge.report4s;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import org.apache.commons.io.*;
import org.openqa.selenium.*;

/**
 * The reporter class.<br>
 * <b style='color:#006600'>This is the class final users need to interact with.</b>
 * @author Harmin Parra Rueda
 *
 */
public class Report4s {

	/**
	 * The name of the Report4s jar file.
	 */
	final protected static String jarfile = "report4s-4.0.jar";
	
	/**
	 * The report directory relative to the working directory.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default values is "report".
	 */
	protected static String report_dir;
	
	/**
	 * The title of the report homepage.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default values is "Test Execution Summary".
	 */
	protected static String report_title;
	
	/**
	 * The filename of the report homepage.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default values is "index.html".
	 */	
	protected static String report_homepage;
	
	/**
	 * The excel report filename.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default values is "Test report.xlsx".
	 */
	protected static String report_excel;
	
	/**
	 * Whether to enable screenshots.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default value is <code>true</code>.
	 */
	public static boolean screenshots;
		
	/**
	 * Padding in pixels to be applied to the web element screenshots.<br>
	 * Defines the area around the web element to be included in the screenshot.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default value is 0.
	 */
	public static int padding;
	
	/**
	 * The number of decimals of precision to format execution time labels.<br>
	 * The value should be between 0 and 3.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default value is 0.
	 */
	public static int time_precision;
	
	/**
	 * The aggregation to be displayed in the pie chart.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default value is "test".
	 */
	protected static String piechart_aggregation;

	/**
	 * Whether to display tooltips with the execution result for each individual suite.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default value is <code>true</code>.
	 */
	protected static boolean suite_tooltips;
	
	/**
	 * Whether to display piecharts or icons for each individual suite in the status column.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default value is "piechart".
	 */
	protected static String suite_status_content;

	/** 
	 * The width and height value in pixels for the piecharts in the status column.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default value is 35px.
	 */
	protected static int suite_status_size;

	/**
	 * Whether to skip the rest of the test if a step is logged as failed.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default value is <code>false</code>.
	 */
	public static boolean skipTestAfterStepFailure;
	
	/**
	 * Whether to skip the remaining tests of the suite if a test fails.<br>
	 * This parameter is read from the <code>report4s.properties</code> file.<br>
	 * The default value is <code>false</code>.
	 */
	public static boolean skipSuiteAfterTestFailure;
	
	/**
	 * Whether the resources has been successfully extracted from the report4s jar file.
	 */
	protected static boolean extracted;
	
	private Report4s() { }
		
	//Initialize the report properties
	static {
		String workspace_dir = System.getProperty("user.dir");
		//Default properties values
		report_dir = workspace_dir + File.separator + "report";
		report_homepage = "index.html";
		report_title = "Test Execution Summary";
		report_excel = "Test report.xlsx";
		screenshots = true;
		padding = 0;
		time_precision = 0;
		piechart_aggregation = "test";
		suite_tooltips = true;
		suite_status_content = "icon";
		suite_status_size = 35;
		skipTestAfterStepFailure = false;
		skipSuiteAfterTestFailure = false;

		//Read and load the properties file.
		Properties prop= new Properties();
		InputStream input = null;
		String parameter;
		
		try {
			input = new FileInputStream("report4s.properties");
			prop.load(input);
		} catch (Exception e) {
			System.err.println("Failed to open and load report4s.properties file");
		}
		
		parameter = prop.getProperty("report4s.report.dir");
		if( parameter != null && !parameter.isEmpty() )
			report_dir = workspace_dir + File.separator + parameter;
		
		parameter = prop.getProperty("report4s.report.homepage");
		if( parameter != null && !parameter.isEmpty() )
			report_homepage = parameter;
		
		parameter = prop.getProperty("report4s.report.title");
		if( parameter != null && !parameter.isEmpty() )
			report_title = parameter;
		
		parameter = prop.getProperty("report4s.report.excel");
		if( parameter != null && !parameter.isEmpty() )
			report_excel = parameter;

		parameter = prop.getProperty("report4s.screenshots.enabled");
		if( parameter != null && (parameter.equalsIgnoreCase("true") || parameter.equalsIgnoreCase("false")) )
			screenshots = Boolean.parseBoolean(parameter);

		parameter = prop.getProperty("report4s.screenshots.padding");
		if( parameter != null ) {
			try { padding = Integer.parseInt(parameter); }
			catch (NumberFormatException e) { }
			padding = padding >= 0 ? padding : 0;
		}
		
		parameter = prop.getProperty("report4s.time.precision");
		if( parameter != null ) {
			try { time_precision = Integer.parseInt(parameter); }
			catch (NumberFormatException e){ }
			time_precision = (time_precision >= 0 && time_precision <= 3) ? time_precision : 0;
		}

		parameter = prop.getProperty("report4s.piechart.aggregation");
		if( parameter != null )
			piechart_aggregation = (parameter.equals("suite") || parameter.equals("test") || parameter.equals("both")) ? parameter : piechart_aggregation;

		parameter = prop.getProperty("report4s.suite.tooltips.enabled");
		if( parameter != null && (parameter.equalsIgnoreCase("true") || parameter.equalsIgnoreCase("false")) )
			suite_tooltips = Boolean.parseBoolean(parameter);

		parameter = prop.getProperty("report4s.suite.status.content");
		if( parameter != null )
			suite_status_content = (parameter.equals("piechart") || parameter.equals("icon")) ? parameter : suite_status_content;

		parameter = prop.getProperty("report4s.suite.status.size");
		if( parameter != null ) {
			parameter = parameter.endsWith("px") ? parameter.substring(0, parameter.length()-2) : parameter;
			try { suite_status_size = Integer.parseInt(parameter); }
			catch (NumberFormatException e) { }
			suite_status_size = suite_status_size >= 0 ? suite_status_size : 35;
		}

		parameter = prop.getProperty("report4s.execution.skipTestAfterStepFailure");
		if( parameter != null && (parameter.equalsIgnoreCase("true") || parameter.equalsIgnoreCase("false")) )
			skipTestAfterStepFailure = Boolean.parseBoolean(parameter);

		parameter = prop.getProperty("report4s.execution.skipSuiteAfterTestFailure");
		if( parameter != null && (parameter.equalsIgnoreCase("true") || parameter.equalsIgnoreCase("false")) )
			skipSuiteAfterTestFailure = Boolean.parseBoolean(parameter);

		//Close the properties file.
		if( input != null ) {
			try { input.close(); }
			catch (IOException e) { System.err.println("Failed to close report4s.properties file"); }
		}

		//Delete any pre-existing report directory
		try { FileUtils.deleteDirectory(new File(report_dir)); }
		catch (IOException e) { System.err.println("Failed to delete existing report directory"); }
		
		//Re-create a new report directory with its resource files
		if( SuiteListener.registered && TestListener.registered ) {
			new File(report_dir).mkdir();
			extractResourcesFromJAR();
		}
	}	
	
	/**
	 * Whether the current suite is multi-threaded.
	 * @return Whether the current suite is multi-threaded.
	 */
	private static boolean isMultiThreadedSuite() {
		return SuiteListener.multi_threaded;
	}

	/**
	 * Whether a {test|configuration} method is running.
	 * @return Whether a {test|configuration} method is running.
	 */
	private static boolean executingTest() {
		return( SuiteListener.registered &&
				( (TestListener.registered && TestListener.running) ||
				  (ConfigurationListener.registered && ConfigurationListener.running) ) );
	}
	
	/**
	 * Log a message after an event is triggered.<br>
	 * The events that are taken into account are :<br>
	 * <br>
	 * <code>
	 * {@link org.openqa.selenium.support.ui.Select#deselectByIndex-int- Select.deselectByIndex(int)}<br>
	 * {@link org.openqa.selenium.support.ui.Select#deselectByValue-java.lang.String- Select.deselectByValue(java.lang.String)}<br>
	 * {@link org.openqa.selenium.support.ui.Select#deselectByVisibleText-java.lang.String- Select.deselectByVisibleText(java.lang.String)}<br>
	 * <br>
	 * {@link org.openqa.selenium.support.ui.Select#selectByIndex-int- Select.selectByIndex(int)}<br>
	 * {@link org.openqa.selenium.support.ui.Select#selectByValue-java.lang.String- Select.selectByValue(java.lang.String)}<br>
	 * {@link org.openqa.selenium.support.ui.Select#selectByVisibleText-java.lang.String- Select.selectByVisibleText(java.lang.String)}<br>
	 * <br>
	 * {@link org.openqa.selenium.WebDriver#get-java.lang.String- WebDriver.get(java.lang.String)}<br>
	 * <br>
	 * {@link org.openqa.selenium.WebDriver.Navigation#back-- WebDriver.navigate().back()}<br>
	 * {@link org.openqa.selenium.WebDriver.Navigation#forward-- WebDriver.navigate().forward()}<br>
	 * {@link org.openqa.selenium.WebDriver.Navigation#refresh-- WebDriver.navigate().refresh()}<br>
	 * {@link org.openqa.selenium.WebDriver.Navigation#to-java.lang.String- WebDriver.navigate().to(java.lang.String)}<br>
	 * {@link org.openqa.selenium.WebDriver.Navigation#to-java.net.URL- WebDriver.navigate().to(java.net.URL)}<br>
	 * <br>
	 * {@link org.openqa.selenium.WebElement#click-- WebElement.click()}<br>
	 * {@link org.openqa.selenium.WebElement#sendKeys-java.lang.CharSequence...- WebElement.sendKeys(CharSequence[])}<br>
	 * </code>
	 * <br>
	 * @param message The message to log.
	 * @param screenshot Whether to include a full web page screenshot.
	 */
	public static void logEvent(String message, boolean screenshot) {
		Logger.logEvent(message, screenshot);
	}
	
	/**
	/**
	 * Log a message, with a full web page screenshot, after an event is triggered.<br>
	 * The events that are taken into account are :<br>
	 * <br>
	 * <code>
	 * {@link org.openqa.selenium.support.ui.Select#deselectByIndex-int- Select.deselectByIndex(int)}<br>
	 * {@link org.openqa.selenium.support.ui.Select#deselectByValue-java.lang.String- Select.deselectByValue(java.lang.String)}<br>
	 * {@link org.openqa.selenium.support.ui.Select#deselectByVisibleText-java.lang.String- Select.deselectByVisibleText(java.lang.String)}<br>
	 * <br>
	 * {@link org.openqa.selenium.support.ui.Select#selectByIndex-int- Select.selectByIndex(int)}<br>
	 * {@link org.openqa.selenium.support.ui.Select#selectByValue-java.lang.String- Select.selectByValue(java.lang.String)}<br>
	 * {@link org.openqa.selenium.support.ui.Select#selectByVisibleText-java.lang.String- Select.selectByVisibleText(java.lang.String)}<br>
	 * <br>
	 * {@link org.openqa.selenium.WebDriver#get-java.lang.String- WebDriver.get(java.lang.String)}<br>
	 * <br>
	 * {@link org.openqa.selenium.WebDriver.Navigation#back-- WebDriver.navigate().back()}<br>
	 * {@link org.openqa.selenium.WebDriver.Navigation#forward-- WebDriver.navigate().forward()}<br>
	 * {@link org.openqa.selenium.WebDriver.Navigation#refresh-- WebDriver.navigate().refresh()}<br>
	 * {@link org.openqa.selenium.WebDriver.Navigation#to-java.lang.String- WebDriver.navigate().to(java.lang.String)}<br>
	 * {@link org.openqa.selenium.WebDriver.Navigation#to-java.net.URL- WebDriver.navigate().to(java.net.URL)}<br>
	 * <br>
	 * {@link org.openqa.selenium.WebElement#click-- WebElement.click()}<br>
	 * {@link org.openqa.selenium.WebElement#sendKeys-java.lang.CharSequence...- WebElement.sendKeys(CharSequence[])}<br>
	 * </code>
	 * <br>
	 * @param message The message to log.
	 */
	public static void logEvent(String message) {
		Logger.logEvent(message, true);
	}
	
	/**
	 * Log a message.<br>
	 * Intended (but not limited) for the log levels: <code>INFO</code>, <code>WARNING</code> and <code>DEBUG</code>.
	 * @param level The log level.
	 * @param message The message to log.
	 */
	public static void logMessage(Level level, String message) {
		log(level, message, null, null, 0);
	}

	/**
	 * Log a message with a full web page screenshot.<br>
	 * Intended (but not limited) for the log levels: <code>INFO</code>, <code>WARNING</code> and <code>DEBUG</code>.
	 * @param level The log level.
	 * @param message The message to log.
	 * @param driver The {@link org.openqa.selenium.WebDriver WebDriver} object.
	 */
	public static void logMessage(Level level, String message, org.openqa.selenium.WebDriver driver) {
		log(level, message, driver, null, 0);
	}

	/**
	 * Log a message with a web element screenshot.<br>
	 * Intended (but not limited) for the log levels: <code>INFO</code>, <code>WARNING</code> and <code>DEBUG</code>.
	 * @param level The log level.
	 * @param message The message to log.
	 * @param driver The {@link org.openqa.selenium.WebDriver WebDriver} object.
	 * @param element The {@link org.openqa.selenium.WebElement WebElement} object.
	 */
	public static void logMessage(Level level, String message, WebDriver driver, WebElement element) {
		log(level, message, driver, element, 0);
	}

	/**
	 * Log a message with a web element screenshot.<br>
	 * Intended (but not limited) for the log levels: <code>INFO</code>, <code>WARNING</code> and <code>DEBUG</code>.
	 * @param level The log level.
	 * @param message The message to log.
	 * @param driver The {@link org.openqa.selenium.WebDriver WebDriver} object.
	 * @param element The {@link org.openqa.selenium.WebElement WebElement} object.
	 * @param padding The extra padding to be applied in addition to the global padding value defined in the <code>report4s.properties</code> file.
	 */
	public static void logMessage(Level level, String message, WebDriver driver, WebElement element, int padding) {
		log(level, message, driver, element, padding);
	}

	/**
	 * Append a log to the test report.
	 * @param level The log level.
	 * @param message The message to log.
	 * @param driver The WebDriver object.
	 * @param element The WebElement object.
	 * @param padding The extra padding to be applied in addition to the global padding value defined in the <code>report4s.properties</code> file.
	 */
	protected static void log(Level level, String message, WebDriver driver, WebElement element, int padding) {
		if( isMultiThreadedSuite() || !executingTest() )
			return;
		//Get the screenshot <a> tag
		String link = Utils.getScreenshotTag(level, driver, element, padding);
		//Get the icon <img> tag and label
		String icon = Utils.getIconTag(level);
		//Print the log
		HtmlWriter.printTableRow(Utils.getLogTag(icon, message, link));
		//Skip the rest of the test if the necessary conditions are met.
		if( level == Level.FAILED && Report4s.skipTestAfterStepFailure )
			throw new SkipTestException();
	}	

	/**
	 * Append an exception to the test report.<br>
	 * The TRACE level is set automatically for this log.
	 * @param error The exception trace to log.
	 */
	protected static void logTrace(Throwable error) {
		int traceCount = ++SuiteListener.traceCount;
		HtmlWriter.printTableRow(Utils.getTraceTag(error, traceCount));
	}

	/**
	 * Extract resources from report4s.jar file needed for the html reports.
	 */
	private static void extractResourcesFromJAR(){
		String jar_file = null;
		try {
			String classPath = System.getProperty("java.class.path");
			String[] pathElements = classPath.split(System.getProperty("path.separator"));
			for( String element : pathElements ) {
				if( element.endsWith(jarfile) ) {
					jar_file = element;
					break;
				}
			}
			if( jar_file == null )
				throw new IOException();
			
			JarFile jar = new JarFile(jar_file);
			Enumeration<JarEntry> enumEntries = jar.entries();
			while( enumEntries.hasMoreElements() ) {
				JarEntry file = enumEntries.nextElement();
				if( !file.getName().startsWith("resources") )
					continue;
				if( file.getName().startsWith("resources/excel") )
					continue;
				File f = new File(report_dir + File.separator + file.getName());
				if( file.isDirectory() ) {  // if it's a directory, then create it
					f.mkdir();
					continue;
				}
				InputStream is = jar.getInputStream(file);  // get the input stream
				FileOutputStream fos = new FileOutputStream(f);
				while( is.available() > 0 )  // write contents of 'is' to 'fos'
					fos.write(is.read());
				fos.close();
				is.close();
			}
			jar.close();
			Report4s.extracted = true;
		} catch (IOException e) {
			if( jar_file == null )
				System.err.println("FATAL ERROR: Failed to locate " + jarfile + " file");
			else
				System.err.println("FATAL ERROR: Failed to extract resources from " + jarfile + " file");
		}
	}

}
