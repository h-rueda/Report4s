package com.github.report4s;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * The reporter class.<br/>
 * <b style='color:#006600'>This is the class final users need to interact with.</b>
 *
 */
public class Report4s {

    /**
     * The report directory relative to the working directory.<br/>
     * This parameter is read from the <code>report4s.properties</code> file.<br/>
     * The default values is "report".
     */
    protected static String report_dir;

    /**
     * The title of the report homepage.<br/>
     * This parameter is read from the <code>report4s.properties</code> file.<br/>
     * The default values is "Test Execution Summary".
     */
    protected static String report_title;

    /**
     * The filename of the report homepage.<br/>
     * This parameter is read from the <code>report4s.properties</code> file.<br/>
     * The default values is "index.html".
     */    
    protected static String report_homepage;

    /**
     * The screenshots to gather.<br/>
     * This parameter is read from the <code>report4s.properties</code> file.<br/>
     * Values: "all", "last", "failed" or "none".<br/>
     * The default value is "all".
     */
    public static String screenshots;

    /**
     * Whether the target for screenshots are pages or WebElements.<br/>
     * This parameter is read from the <code>report4s.properties</code> file.<br/>
     * Values: "page" or "element".<br/>
     * The default value is "page".
     */
    public static String target;

    /**
     * Padding in pixels to be applied to the web element screenshots.<br/>
     * Defines the area around the web element to be included in the screenshot.<br/>
     * This parameter is read from the <code>report4s.properties</code> file.<br/>
     * The default value is 10.
     */
    public static int padding;

    /**
     * The number of decimals of precision to format execution time labels.<br/>
     * This parameter is read from the <code>report4s.properties</code> file.<br/>
     * The value should be between 0 and 3.<br/>
     * The default value is 0.
     */
    public static int time_precision;

    /**
     * Whether to display tooltips with the execution result for each individual suite.<br/>
     * This parameter is read from the <code>report4s.properties</code> file.<br/>
     * The default value is <code>true</code>.
     */
    protected static boolean suite_tooltips;

    /**
     * Whether to skip the remaining tests of the suite if a test fails.<br/>
     * This parameter is read from the <code>report4s.properties</code> file.<br/>
     * The default value is <code>false</code>.
     */
    public static boolean skipSuiteAfterTestFailure;

    /**
     * Whether the assets has been successfully extracted from the report4s jar file.
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
        screenshots = "all";
        target = "page";
        padding = 10;
        time_precision = 0;
        suite_tooltips = true;
        skipSuiteAfterTestFailure = true;

        //Read and load the properties file.
        Properties prop= new Properties();
        InputStream input = null;
        String parameter;

        try {
            input = new FileInputStream("report4s.properties");
            prop.load(input);

            parameter = prop.getProperty("report4s.report.dir");
            if (parameter != null && !parameter.isEmpty())
                report_dir = workspace_dir + File.separator + parameter;

            parameter = prop.getProperty("report4s.report.homepage");
            if (parameter != null && !parameter.isEmpty())
                report_homepage = parameter;

            parameter = prop.getProperty("report4s.report.title");
            if (parameter != null && !parameter.isEmpty())
                report_title = parameter;

            parameter = prop.getProperty("report4s.screenshots.enabled");
            if(parameter != null
                    && (parameter.equalsIgnoreCase("all")
                        || parameter.equalsIgnoreCase("failed")
                        || parameter.equalsIgnoreCase("last")
                        || parameter.equalsIgnoreCase("none")))
                screenshots = parameter.toLowerCase();

            parameter = prop.getProperty("report4s.screenshots.target");
            if (StringUtils.equalsIgnoreCase(parameter, "page") || StringUtils.equalsIgnoreCase(parameter, "element"))
                target = parameter.toLowerCase();

            parameter = prop.getProperty("report4s.screenshots.padding");
            if (parameter != null) {
                try { padding = Integer.parseInt(parameter); }
                catch (NumberFormatException e) { }
                padding = padding >= 0 ? padding : 10;
            }

            parameter = prop.getProperty("report4s.time.precision");
            if (parameter != null) {
                try { time_precision = Integer.parseInt(parameter); }
                catch (NumberFormatException e){ }
                time_precision = (time_precision >= 0 && time_precision <= 3) ? time_precision : 0;
            }

            parameter = prop.getProperty("report4s.suite.tooltips.enabled");
            if (parameter != null && (parameter.equalsIgnoreCase("true") || parameter.equalsIgnoreCase("false")))
                suite_tooltips = Boolean.parseBoolean(parameter);

            parameter = prop.getProperty("report4s.execution.skipSuiteAfterTestFailure");
            if (parameter != null && (parameter.equalsIgnoreCase("true") || parameter.equalsIgnoreCase("false")))
                skipSuiteAfterTestFailure = Boolean.parseBoolean(parameter);

            //Close the properties file.
            input.close();
        } catch (FileNotFoundException e) {
            System.err.println("INFO: Failed to open report4s.properties file");
        } catch (IOException e) {
            System.err.println("INFO: Failed to load/close report4s.properties file");
        }

        //Delete any pre-existing report directory
        try { FileUtils.deleteDirectory(new File(report_dir)); }
        catch (IOException e) { System.err.println("Failed to delete existing report directory"); }

        //Re-create a new report directory with its assets files
        if (SuiteListener.registered && TestListener.registered) {
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
        return (SuiteListener.registered
                && ((TestListener.registered && TestListener.running)
                        || (ConfigurationListener.registered && ConfigurationListener.running)));
    }

    /**
     * Log a message.<br/>
     * Intended (but not limited) for the log levels: <code>INFO</code>, <code>WARNING</code> and <code>DEBUG</code>.
     * @param level The log level.
     * @param message The message to log.
     */
    public static void logMessage(Level level, String message) {
        log(level, message, null, null);
    }

    /**
     * Log a message with a full web page screenshot.<br/>
     * Intended (but not limited) for the log levels: <code>INFO</code>, <code>WARNING</code> and <code>DEBUG</code>.
     * @param level The log level.
     * @param message The message to log.
     * @param driver The {@link org.openqa.selenium.WebDriver WebDriver} object.
     */
    public static void logMessage(Level level, String message, WebDriver driver) {
        log(level, message, driver, null);
    }

    /**
     * Log a message with a web element screenshot.<br/>
     * Intended (but not limited) for the log levels: <code>INFO</code>, <code>WARNING</code> and <code>DEBUG</code>.
     * @param level The log level.
     * @param message The message to log.
     * @param driver The {@link org.openqa.selenium.WebDriver WebDriver} object.
     * @param element The {@link org.openqa.selenium.WebElement WebElement} object.
     */
    public static void logMessage(Level level, String message, WebDriver driver, WebElement element) {
        log(level, message, driver, element);
    }

    /**
     * Log a message with a web element screenshot.<br/>
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
    protected static void log(Level level, String message, WebDriver driver, WebElement element) {
        log(level, message, driver, element, Report4s.padding);
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
        if (isMultiThreadedSuite() || !executingTest())
            return;
        //Get the screenshot <a> tag
        String link = Utils.getScreenshotTag(level, driver, element, padding);
        //Get the icon <img> tag and label
        String icon = Utils.getIconTag(level);
        //Print the log
        HtmlWriter.printTableRow(Utils.getLogTag(icon, message, link));
    }    

    /**
     * Append an exception to the test report.<br/>
     * The TRACE level is set automatically for this log.
     * @param error The exception trace to log.
     */
    protected static void logTrace(Throwable error) {
        int traceCount = ++SuiteListener.traceCount;
        TestListener.exception_logged = true;
        HtmlWriter.printTableRow(Utils.getTraceTag(error, traceCount));
    }

    /**
     * Extract assets from report4s.jar file needed for the html reports.
     */
    private static void extractResourcesFromJAR(){
        String jar_file = null;
        try {
            String classPath = System.getProperty("java.class.path");
            String[] pathElements = classPath.split(System.getProperty("path.separator"));
            for (String element : pathElements) {
                int i = element.lastIndexOf(System.getProperty("file.separator"));
                String file = (i == -1) ? element : element.substring(i+1);
                if (file.startsWith("report4s") && file.endsWith(".jar")) {
                    jar_file = element;
                    break;
                }
            }
            if (jar_file == null)
                throw new IOException();

            JarFile jar = new JarFile(jar_file);
            Enumeration<JarEntry> enumEntries = jar.entries();
            while (enumEntries.hasMoreElements()) {
                JarEntry file = enumEntries.nextElement();
                if (!file.getName().startsWith("assets"))
                    continue;
                if (file.getName().startsWith("assets/excel"))
                    continue;
                File f = new File(report_dir + File.separator + file.getName());
                if (file.isDirectory()) {
                    f.mkdir();
                    continue;
                }
                InputStream is = jar.getInputStream(file);
                FileOutputStream fos = new FileOutputStream(f);
                while (is.available() > 0)
                    fos.write(is.read());
                fos.close();
                is.close();
            }
            jar.close();
            Report4s.extracted = true;
        } catch (IOException e) {
            if (jar_file == null)
                System.err.println("FATAL ERROR: Failed to locate report4s JAR file");
            else
                System.err.println("FATAL ERROR: Failed to extract assets from report4s JAR file");
        }
    }

}
