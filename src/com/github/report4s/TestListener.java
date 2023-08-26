package com.github.report4s;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.TestListenerAdapter;

/**
 * The test listener.
 */
public class TestListener extends TestListenerAdapter {

    /**
     * Whether this listener has been registered in the testng xml file.
     */
    protected static boolean registered;

    /**
     * Whether a test method is running.
     */
    protected static boolean running;

    /**
     * Whether a log has been added to the HTML test report.
     */
    protected static boolean printing;

    /**
     * Whether an exception has been logged in the HTML test report.
     */
    protected static boolean exception_logged = false;

    /**
     * Whether an event has been logged in the HTML test report.
     */
    protected static boolean event_logged = false;

    /**
     * Whether an HTML table has been opened in the HTML test report.
     */
    protected static boolean opened_table = false;
    
    /**
     * The WebDriver being used during the test execution.
     */
    protected static WebDriver driver = null;

    /**
     * An instantiation indicates that the listener was added in the testng xml file.
     */
    public TestListener() {
        registered = true;
    }

    /**
     * Invoked each time before a test method is invoked.
     */
    @Override
    public void onTestStart(ITestResult result) {
        if (!verifyPrecondition())
            return;

        //Force the test to skip if the necessary conditions are met.
        if (SuiteListener.test_failure && Report4s.skipSuiteAfterTestFailure) {
            if (Utils.getDependencies(result).length > 0) {
                startTestReport(result);
                return;
            }
            result.setEndMillis(result.getStartMillis());
            result.setStatus(ITestResult.SKIP);
            HtmlWriter.printTestTitle(result);
            throw new SkipSuiteException();
        }
        //Otherwise, start the test
        SuiteListener.methodCount++;
        TestListener.exception_logged = false;
        TestListener.event_logged = false;
        startTestReport(result);
    }

    /**
     * Invoked each time a test method succeeds.
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        if (!verifyPrecondition())
            return;
        if (StringUtils.equals(Report4s.screenshots, "last"))
        	Report4s.logMessage(Level.PASSED, "Last screenshot", TestListener.driver);
        TestListener.event_logged = false;
        endTestReport(result);
    }

    /**
     * Invoked each time a test method fails.
     */
    @Override
    public void onTestFailure(ITestResult result) {
        if (!verifyPrecondition())
            return;
        if (!(result.getThrowable() instanceof SkipSuiteException)
                || (result.getThrowable() instanceof SkipException)) {
            //Print the exception trace in the standard error output device.
            result.getThrowable().printStackTrace();
            //Print the exception trace in the test report.
            if (!TestListener.event_logged)
                Report4s.logMessage(Level.FAILED, "Last screenshot before failure", TestListener.driver);
            if (!TestListener.exception_logged)
                Report4s.logTrace(result.getThrowable());
        }
        TestListener.event_logged = false;
        SuiteListener.test_failure = true;
        endTestReport(result);
    }

    /**
     * Invoked each time a test method is skipped.
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        if (!verifyPrecondition())
            return;
        if (StringUtils.equals(Report4s.screenshots, "last"))
            Report4s.logMessage(Level.INFO, "Last screenshot before skip", TestListener.driver);
        if (result.getThrowable() != null && result.getThrowable() instanceof SkipException)
            Report4s.logTrace(result.getThrowable());
        TestListener.event_logged = false;
        endTestReport(result);
    }

    /**
     * Invoked each time a test method fails within the success percentage.
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        if (!verifyPrecondition())
            return;
        TestListener.event_logged = false;
        endTestReport(result);
    }

    /**
     * Print the table header of a test method report.
     */
    private void startTestReport(ITestResult result) {
        HtmlWriter.printTestTitle(result);
        HtmlWriter.openTable();
        opened_table = true;
        running = true;
    }

    /**
     * Print the closing </table> tag and the execution time label of a test method report.
     */
    private void endTestReport(ITestResult result) {
        Status status = Utils.getTestStatus(result);

        if (opened_table)
            HtmlWriter.closeTable();
        if (status != Status.SKIPPED) {
            //Print the execution time.
            long time = result.getEndMillis() - result.getStartMillis();
            HtmlWriter.printExecutionTime(Utils.getExecutionLabel(time));
        }
        //Print the test status
        if (status == Status.FAILED || status == Status.SKIPPED)
            HtmlWriter.printTestStatus(status);
        if (status == Status.FAILED_WITHIN_PERCENTAGE)
            HtmlWriter.printTestStatus(status, Utils.getSuccessPercentage(result));

        HtmlWriter.printLineBreak();

        running = false;
        printing = false;
        opened_table = false;
        addMetadata(result);
    }

    /**
     * Add meta-data of a test method for the html and Excel reports.
     * @param result The test under execution.
     */
    private void addMetadata(ITestResult result) {
        String method_name = Utils.getMethod(result);
        String method_parameters = Utils.getParameters(result);
        Status status = null;
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                status = Status.PASSED;
                break;
            case ITestResult.FAILURE:
                status = Status.FAILED;
                break;
            case ITestResult.SKIP:
                status = Status.SKIPPED;
                break;
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
                status = Status.FAILED_WITHIN_PERCENTAGE;
                break;
        }
        long time = result.getEndMillis() - result.getStartMillis();
        int order = SuiteListener.methodCount;
        Metadata.addMethod(method_name, method_parameters, status, time, order);
    }

    /**
     * Whether the conditions are met before logging.
     * @return
     */
    private boolean verifyPrecondition() {
        return Report4s.extracted && !SuiteListener.multi_threaded;
    }

}
